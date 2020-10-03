package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class PsqlMain {
    Store store = PsqlStore.instOf();

    public static void main(String[] args) {
        PsqlMain psql = new PsqlMain();
        psql.saveNewPost();
        psql.saveNewCandidate();
        Post post = psql.findPostById(1);
        post.setName("Kotlin developer");
        psql.updatePost(post);
        Candidate candidate = psql.findCandidateById(1);
        candidate.setName("Java/Kotlin middle");
        psql.updateCandidate(candidate);
    }

    private Candidate findCandidateById(int i) {
        return store.findCandidateById(i);
    }

    private Post findPostById(int i) {
        return store.findById(i);
    }

    private void saveNewPost() {
        store.save(new Post(0, "Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
    }

    private void saveNewCandidate() {
        store.save(new Candidate(0, "Java Job candidate"));
        for (Candidate can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName());
        }
    }

    private void updatePost(Post post) {
        store.save(post);
    }

    private void updateCandidate(Candidate can) {
        store.save(can);
    }
}
