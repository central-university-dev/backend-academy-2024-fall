package backend.academy.seminar8.notebook.di;

import java.util.List;
import java.util.UUID;

public interface NotebookService {
    Note addNoteToNotebook(String notebookName, String title, String content);

    List<Note> getNotesFromNotebook(String notebookName);

    void deleteNoteFromNotebook(String notebookName, UUID noteId);

    List<Note> searchNotes(String query);
}
