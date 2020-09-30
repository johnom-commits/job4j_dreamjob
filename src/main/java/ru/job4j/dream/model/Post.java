package ru.job4j.dream.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private int id;
    private String name;
    private String description;
    private LocalDateTime created = LocalDateTime.now();

    public Post(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
