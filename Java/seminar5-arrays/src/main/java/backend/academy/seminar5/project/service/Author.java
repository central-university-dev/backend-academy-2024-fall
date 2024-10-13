package backend.academy.seminar5.project.service;

import java.util.Objects;

public class Author {
    private long id;
    private String name;
    private String biography;

    public Author(long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Author author = (Author) object;
        return id == author.id
               && Objects.equals(name, author.name)
               && Objects.equals(biography, author.biography);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(biography);
        return result;
    }
}
