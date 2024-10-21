package backend.academy.seminar8.notebook.di;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotebookServiceImpl implements NotebookService {
    private final Storage storage;
    private final TimeHelper timeHelper;
    private final UUIDHelper uuidHelper;

    @Inject
    public NotebookServiceImpl(Storage storage, TimeHelper timeHelper, UUIDHelper uuidHelper) {
        this.storage = storage;
        this.timeHelper = timeHelper;
        this.uuidHelper = uuidHelper;
    }

    @Override
    public Note addNoteToNotebook(String notebookName, String title, String content) {
        Instant now = timeHelper.now();
        Note note = new Note(uuidHelper.randomUUID(), title, content);
        note.setCreatedAt(now);
        Notebook notebook = storage.findNotebookByName(notebookName)
            .orElseGet(() -> new Notebook(notebookName));
        notebook.getNotes().add(note);
        notebook.setCreatedAt(now);
        notebook.setUpdatedAt(now);
        storage.saveNotebook(notebook);
        return note;
    }

    @Override
    public List<Note> getNotesFromNotebook(String notebookName) {
        return storage.findNotebookByName(notebookName).map(Notebook::getNotes).orElseGet(ArrayList::new);
    }

    @Override
    public void deleteNoteFromNotebook(String notebookName, UUID noteId) {
        Instant now = timeHelper.now();
        Optional<Notebook> notebookByName = storage.findNotebookByName(notebookName);
        if (notebookByName.isPresent()) {
            Notebook notebook = notebookByName.get();
            if (notebook.getNotes().removeIf(note -> note.getId().equals(noteId))) {
                notebook.setUpdatedAt(now);
                storage.saveNotebook(notebook);
            }
        }
    }

    @Override
    public List<Note> searchNotes(String query) {
        return storage.searchNotes(query);
    }
}
