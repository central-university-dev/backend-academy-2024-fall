package backend.academy.seminar8.notebook.nodi;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotebookService {

    // Жестко закодированный InMemoryStorage
    private final InMemoryStorage storage = new InMemoryStorage();

    public Note addNoteToNotebook(String notebookName, String title, String content) {
        // Жестко закодированный Instant.now()
        Instant now = Instant.now();
        // Жестко закодированный UUID.randomUUID()
        Note note = new Note(UUID.randomUUID(), title, content);
        note.setCreatedAt(now);
        Notebook notebook = storage.findNotebookByName(notebookName)
            .orElseGet(() -> new Notebook(notebookName));
        notebook.getNotes().add(note);
        notebook.setUpdatedAt(now);
        storage.saveNotebook(notebook);
        return note;
    }

    public List<Note> getNotesFromNotebook(String notebookName) {
        return storage.findNotebookByName(notebookName).map(Notebook::getNotes).orElseGet(ArrayList::new);
    }

    public void deleteNoteFromNotebook(String notebookName, UUID noteId) {
        // Жестко закодированный Instant.now()
        Instant now = Instant.now();
        Optional<Notebook> maybeNotebook = storage.findNotebookByName(notebookName);
        if (maybeNotebook.isPresent()) {
            Notebook notebook = maybeNotebook.get();
            if (notebook.getNotes().removeIf(note -> note.getId().equals(noteId))) {
                notebook.setUpdatedAt(now);
                storage.saveNotebook(notebook);
            }
        }
    }

    public List<Note> searchNotes(String query) {
        return storage.searchNotes(query);
    }
}

