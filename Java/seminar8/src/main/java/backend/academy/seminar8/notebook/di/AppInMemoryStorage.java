package backend.academy.seminar8.notebook.di;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppInMemoryStorage {

    static private final Map<String, Notebook> storage = new HashMap<>();
    static private final TimeHelper timeHelper = new TimeHelperImpl();
    static private final UUIDHelper uuidHelper = new UUIDHelperImpl();

    static class NotebookModule extends AbstractModule {
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
