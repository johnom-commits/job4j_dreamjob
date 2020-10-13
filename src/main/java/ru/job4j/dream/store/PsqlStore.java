package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.jetbrains.annotations.Nullable;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Candidate can = new Candidate(it.getInt("id"), it.getString("name"));
                    can.setPhotoId(it.getInt("photoId"));
                    candidates.add(can);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name, photoid) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    public int getNewPhotoId() {
        try (var con = pool.getConnection();
             var ps = con.prepareStatement("INSERT INTO phote DEFAULT VALUES RETURNING id")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void update(Post post) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("UPDATE post SET name = (?) WHERE id = (?)")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(Candidate candidate) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("UPDATE candidate SET (name, photoid) = (?, ?) WHERE id = (?)")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void changeUserPassword(User user, String newPassword) {

    }

    public void deleteCandidate(int id) {
        int photoId = getPhotoId(id);
        deleteItemFromDB(id);
        deleteImageFromDisk(photoId);
        deletePhotoId(photoId);
    }

    @Override
    public User findByEmail(String email) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("SELECT * FROM users WHERE email = (?)")
        ) {
            ps.setString(1, email);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new User(it.getInt("id"), it.getString("login"), it.getString("email"), it.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User user) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("INSERT INTO users(login, email, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePhotoId(int photoId) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("DELETE FROM phote WHERE id = (?)")
        ) {
            ps.setInt(1, photoId);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteImageFromDisk(int photoId) {
        if (photoId == 0) {
            return;
        }
        String fileName = photoId + ".jpg";
        String file = new File("images").getAbsolutePath() + File.separator + fileName;
        try {
            Files.delete(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPhotoId(int id) {
        int photoId = 0;
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("SELECT photoid FROM candidate WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    photoId = it.getInt("photoid");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoId;
    }

    private void deleteItemFromDB(int id) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("DELETE FROM candidate WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    public Post findById(int id) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("SELECT name FROM post WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            try (var it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(id, it.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("SELECT name FROM candidate WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            try (var it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(id, it.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Candidate(0, "");
    }
}
