package backend.academy.seminar8.notebook.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class InFileStorageTest {

    private InFileStorage inFileStorage;
    private final Cache<String, Notebook> cache = Caffeine.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @TempDir
    private Path tempDir;

    class NotebookModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Path.class).toInstance(tempDir);
            bind(ObjectMapper.class).toInstance(objectMapper);
            bind(new TypeLiteral<Cache<String, Notebook>>() {
            }).toInstance(cache);
        }
    }

    @BeforeEach
    void setUp() {
        Injector injector = Guice.createInjector(new InFileStorageTest.NotebookModule());
        inFileStorage = injector.getInstance(InFileStorage.class);
    }

    @Test
    void findNotebookByName() throws IOException {
        // Given
        Notebook diary = new Notebook("Дневник");
        diary.getNotes().add(new Note(UUID.fromString("edd5183c-dae1-425e-82f5-7ef0efc0165f"), "16.10.24",
            "Целый день придумывал название для переменной"));
        Path notebookFile = tempDir.resolve("Дневник.json");
        objectMapper.writeValue(notebookFile.toFile(), diary);

        // When
        Optional<Notebook> result = inFileStorage.findNotebookByName("Дневник");

        // Then
        assertTrue(result.isPresent());
        assertEquals(diary.getName(), result.get().getName());
        assertEquals(diary.getNotes().size(), result.get().getNotes().size());
        Notebook diaryInCache = cache.getIfPresent("Дневник");
        assertEquals(diary.getName(), diaryInCache.getName());
        assertEquals(diary.getName(), diaryInCache.getName());
    }

    @Test
    void saveNotebook() throws IOException {
        // Given
        Notebook diary = new Notebook("Дневник");
        diary.getNotes().add(new Note(UUID.fromString("edd5183c-dae1-425e-82f5-7ef0efc0165f"), "16.10.24",
            "Целый день придумывал название для переменной"));

        // When
        inFileStorage.saveNotebook(diary);

        // Then
        Path notebookFile = tempDir.resolve("Дневник.json");
        Notebook notebookFromFile = objectMapper.readValue(notebookFile.toFile(), Notebook.class);
        assertEquals(diary.getName(), notebookFromFile.getName());
        Note noteFromFile = notebookFromFile.getNotes().getFirst();
        Note noteInCache = cache.asMap().get("Дневник").getNotes().getFirst();
        assertEquals(noteFromFile.getId(), noteInCache.getId());
        assertEquals(noteFromFile.getTitle(), noteInCache.getTitle());
        assertEquals(noteFromFile.getCreatedAt(), noteInCache.getCreatedAt());
    }

    @Test
    void searchNotes() {
        // Given
        Notebook recipeBook = new Notebook("Книга рецептов");
        Note recipe1 = new Note(UUID.randomUUID(), "Паста с кремовым соусом и овощами",
            "паста, сливки, кабачок, красный перец, пармезан, оливковое масло, соль, перец, базилик");
        Note recipe2 = new Note(UUID.randomUUID(), "Запеченные овощи с сыром",
            "кабачок, красный перец, пармезан, оливковое масло, орегано, соль, перец");
        Note recipe3 = new Note(UUID.randomUUID(), "Суп-пюре из овощей с кремом",
            "кабачок, красный перец, сливки, овощной бульон, оливковое масло, соль, перец");
        recipeBook.getNotes().add(recipe1);
        recipeBook.getNotes().add(recipe2);
        recipeBook.getNotes().add(recipe3);
        cache.put("Книга рецептов", recipeBook);

        // When
        List<Note> notes = inFileStorage.searchNotes("сливки");

        // Then
        assertEquals(2, notes.size());
    }
}
