package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.List;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    Post findById(int id);

    Candidate findCandidateById(int id);

    int getNewPhotoId();

    void deleteCandidate(int id);

    User findByEmail(String email);

    void save(User user);

    void deleteUser(User user);

    void changeUserPassword(User user, String newPassword);

    List<String> getCities();

    int getIdCity(String name);

    String getNameCity(int id);
}
