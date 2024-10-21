package backend.academy.seminar8.notebook.nodi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryStorage {
    private final Map<String, Notebook> notebooks = new HashMap<>();

    public Optional<Notebook> findNotebookByName(String notebookName) {
        return Optional.ofNullable(notebooks.get(notebookName));
    }

    public Notebook saveNotebook(Notebook notebook) {
        return notebooks.put(notebook.getName(), notebook);
    }

    public List<Note> searchNotes(String query) {
        return notebooks.values().stream()
            .map(Notebook::getNotes)
            .flatMap(Collection::stream)
            .filter(it -> it.getTitle().toLowerCase().contains(query) ||
                it.getContent().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }
}
