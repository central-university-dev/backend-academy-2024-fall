package backend.academy.seminar8.notebook.di;

import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryStorage implements Storage {
    private final Map<String, Notebook> notebooks;

    @Inject
    public InMemoryStorage(Map<String, Notebook> notebooks) {
        this.notebooks = notebooks;
    }

    @Override
    public Optional<Notebook> findNotebookByName(String notebookName) {
        return Optional.ofNullable(notebooks.get(notebookName));
    }

    @Override
    public Notebook saveNotebook(Notebook notebook) {
        return notebooks.put(notebook.getName(), notebook);
    }

    @Override
    public List<Note> searchNotes(String query) {
        return notebooks.values().stream()
            .map(Notebook::getNotes)
            .flatMap(Collection::stream)
            .filter(it -> it.getTitle().toLowerCase().contains(query) ||
                it.getContent().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }
}
