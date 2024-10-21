package backend.academy.seminar8.notebook.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.apache.logging.log4j.util.Cast;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppInFileStorage {

    private static final TimeHelper timeHelper = new TimeHelperImpl();
    private static final UUIDHelper uuidHelper = new UUIDHelperImpl();
    private static final Cache<String, Notebook> cache = Caffeine.newBuilder().build();
    private static final Path notebooksFolder = Path.of("seminar8", "src", "main", "resources", "notebooks");
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    static class NotebookModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(NotebookService.class).to(NotebookServiceImpl.class);
            bind(Storage.class).to(InFileStorage.class);
            bind(TimeHelper.class).toInstance(timeHelper);
            bind(UUIDHelper.class).toInstance(uuidHelper);
            bind(Path.class).toInstance(notebooksFolder);
            bind(ObjectMapper.class).toInstance(objectMapper);
            bind(new TypeLiteral<Cache<String, Notebook>>() {
            }).toInstance(cache);
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new NotebookModule());
        NotebookService notebookService = injector.getInstance(NotebookService.class);

        Note note1 = notebookService.addNoteToNotebook("Книга рецептов", "Паста с кремовым соусом и овощами",
            "паста, сливки, кабачок, красный перец, пармезан, оливковое масло, соль, перец, базилик");
        Note note2 = notebookService.addNoteToNotebook("Книга рецептов", "Запеченные овощи с сыром",
            "кабачок, красный перец, пармезан, оливковое масло, орегано, соль, перец");
        Note note3 = notebookService.addNoteToNotebook("Книга рецептов", "Суп-пюре из овощей с кремом",
            "кабачок, красный перец, сливки, овощной бульон, оливковое масло, соль, перец");
        List<Note> notes = notebookService.searchNotes("сливки");
        notebookService.deleteNoteFromNotebook("Книга рецептов", note1.getId());
        List<Note> notes2 = notebookService.getNotesFromNotebook("Книга рецептов");
    }
}
