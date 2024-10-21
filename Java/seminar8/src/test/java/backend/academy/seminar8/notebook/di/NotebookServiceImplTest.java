package backend.academy.seminar8.notebook.di;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotebookServiceImplTest {

    private NotebookService notebookService;
    private final Map<String, Notebook> storage = new HashMap<>();
    private final Instant now = Instant.ofEpochSecond(0);
    private final TimeHelper timeHelper = new TimeHelperMock(now);
    private final UUID uuid = UUID.fromString("edd5183c-dae1-425e-82f5-7ef0efc0165f");
    private final UUIDHelper uuidHelper = new UUIDHelperMock(uuid);

    class NotebookModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(NotebookService.class).to(NotebookServiceImpl.class);
            bind(Storage.class).to(InMemoryStorage.class);
            bind(TimeHelper.class).toInstance(timeHelper);
            bind(UUIDHelper.class).toInstance(uuidHelper);
            bind(new TypeLiteral<Map<String, Notebook>>() {
            }).toInstance(storage);
        }
    }

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new NotebookModule());
        notebookService = injector.getInstance(NotebookService.class);
    }

    @Test
    void addNoteToNotebook() {
        // When
        notebookService.addNoteToNotebook("Дневник", "16.10.24", "Целый день придумывал название для переменной");

        // Then
        Notebook diaryFromStorage = storage.get("Дневник");
        assertEquals("Дневник", diaryFromStorage.getName());
        assertEquals(now, diaryFromStorage.getCreatedAt());
        assertEquals(now, diaryFromStorage.getUpdatedAt());

        Note noteFromStorage = diaryFromStorage.getNotes().get(0);
        assertEquals(uuid, noteFromStorage.getId());
        assertEquals("16.10.24", noteFromStorage.getTitle());
        assertEquals("Целый день придумывал название для переменной", noteFromStorage.getContent());
        assertEquals(now, diaryFromStorage.getCreatedAt());
        assertEquals(now, diaryFromStorage.getUpdatedAt());
    }

    @Test
    void getNotesFromNotebook() {
        // Given
        Notebook notebook = new Notebook("Дневник");
        Note note = new Note(UUID.randomUUID(), "16.10.24", "Целый день придумывал название для переменной");
        notebook.getNotes().add(note);
        storage.put("Дневник", notebook);

        // When
        List<Note> notes = notebookService.getNotesFromNotebook("Дневник");

        // Then
        assertTrue(notes.contains(note));
    }

    @Test
    void deleteNoteFromNotebook() {
        // Given
        Notebook notebook = new Notebook("Дневник");
        Note note = new Note(UUID.randomUUID(), "16.10.24", "Целый день придумывал название для переменной");
        notebook.getNotes().add(note);
        storage.put("Дневник", notebook);

        // WHen
        notebookService.deleteNoteFromNotebook("Дневник", note.getId());

        // Then
        assertTrue(storage.get("Дневник").getNotes().isEmpty());
    }

    @Test
    void searchNotes() {
        // Given
        Notebook diary = new Notebook("Дневник");
        Note note1 = new Note(UUID.randomUUID(), "16.10.24", "Целый день придумывал название для переменной");
        Note note2 = new Note(UUID.randomUUID(), "15.10.24", "Изучал стратегии инвалидации кэша");
        diary.getNotes().add(note1);
        diary.getNotes().add(note2);
        storage.put("Дневник", diary);

        Notebook recipeBook = new Notebook("Книга рецептов");
        Note note3 = new Note(UUID.randomUUID(), "Паста с кремовым соусом и овощами",
            "паста, сливки, кабачок, красный перец, пармезан, оливковое масло, соль, перец, базилик");
        Note note4 = new Note(UUID.randomUUID(), "Запеченные овощи с сыром",
            "кабачок, красный перец, пармезан, оливковое масло, орегано, соль, перец");
        Note note5 = new Note(UUID.randomUUID(), "Суп-пюре из овощей с кремом",
            "кабачок, красный перец, сливки, овощной бульон, оливковое масло, соль, перец");
        recipeBook.getNotes().add(note3);
        recipeBook.getNotes().add(note4);
        recipeBook.getNotes().add(note5);
        storage.put("Книга рецептов", recipeBook);

        // When
        List<Note> notes = notebookService.searchNotes("сливки");

        // Then
        assertEquals(2, notes.size());
        assertTrue(notes.contains(note3));
        assertTrue(notes.contains(note5));
    }
}
