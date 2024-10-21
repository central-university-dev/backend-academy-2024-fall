package backend.academy.seminar8.notebook.nodi;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotebookServiceImplTest {

    private NotebookService notebookService = new NotebookService();

    @Test
    void addNoteToNotebook() {
        // When
        Note note =
            notebookService.addNoteToNotebook("Дневник", "16.10.24",
                "Целый день придумывал название для переменной");

        // Then
        assertNotNull(note);
        // Как проверить, что заметка действительно сохранилась?
    }

    @Test
    void getNotesFromNotebook() {
        // Given
        // Неудобно использовать метод тестируемого сервиса для создания данных в тесте.
        // А что, если addNoteToNotebook, помимо сохранения заметки в хранилище, также отправляет электронное письмо?
        notebookService.addNoteToNotebook("Дневник", "16.10.24", "Целый день придумывал название для переменной");

        // When
        List<Note> notes = notebookService.getNotesFromNotebook("Дневник");

        // Then
        assertEquals(1, notes.size());
        Note note = notes.get(0);
        assertEquals("16.10.24", note.getTitle());
        assertNotNull(note.getCreatedAt()); // Было бы надежнее сделать assertEquals(date, note.getCreatedAt())
        assertEquals("Целый день придумывал название для переменной", note.getContent());
    }

    @Test
    void deleteNoteFromNotebook() {
        // Given
        Note note =
            notebookService.addNoteToNotebook("Дневник", "16.10.24", "Целый день придумывал название для переменной");

        // When
        notebookService.deleteNoteFromNotebook("Дневник", note.getId());

        // Then
        // Как проверить, что заметка действительно была удалена?
    }

    @Test
    void searchNotes() {
        // Given
        notebookService.addNoteToNotebook("Дневник", "15.10.24", "Изучал стратегии инвалидации кэша");
        notebookService.addNoteToNotebook("Дневник", "16.10.24", "Целый день придумывал название для переменной");
        notebookService.addNoteToNotebook("Книга рецептов",
            "Паста с кремовым соусом и овощами",
            "паста, сливки, кабачок, красный перец, пармезан, оливковое масло, соль, перец, базилик"
        );
        notebookService.addNoteToNotebook("Книга рецептов",
            "Запеченные овощи с сыром",
            "кабачок, красный перец, пармезан, оливковое масло, орегано, соль, перец"
        );
        notebookService.addNoteToNotebook("Книга рецептов",
            "Суп-пюре из овощей с кремом",
            "кабачок, красный перец, сливки, овощной бульон, оливковое масло, соль, перец"
        );

        // When
        List<Note> notes = notebookService.searchNotes("сливки");

        // Then
        assertEquals(2, notes.size());
        assertTrue(notes.stream().anyMatch(note -> note.getTitle().equals("Паста с кремовым соусом и овощами")));
        assertTrue(notes.stream().anyMatch(note -> note.getTitle().equals("Суп-пюре из овощей с кремом")));

        // Как понять, что мы действительно получили те заметки?
    }
}
