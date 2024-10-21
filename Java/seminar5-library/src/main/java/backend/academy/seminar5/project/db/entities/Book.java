package backend.academy.seminar5.project.db.entities;

import java.util.EnumSet;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private long id;
    private String isbn;
    private String title;
    private List<Author> authors; // Предполагаем, что у книги может быть несколько авторов
    private EnumSet<BookCategory> categories;
    private int yearPublished;
    private int pageCount;
}
