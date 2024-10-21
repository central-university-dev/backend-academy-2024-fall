package backend.academy.seminar8.notebook.di;

import java.util.List;
import java.util.Optional;

public interface Storage {
    Optional<Notebook> findNotebookByName(String notebookName);

    Notebook saveNotebook(Notebook notebook);

    List<Note> searchNotes(String query);
}
