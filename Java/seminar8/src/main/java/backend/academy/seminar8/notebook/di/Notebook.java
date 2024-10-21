package backend.academy.seminar8.notebook.di;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Notebook {
    private String name;
    private final List<Note> notes = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;

    public Notebook() {
    }

    public Notebook(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
