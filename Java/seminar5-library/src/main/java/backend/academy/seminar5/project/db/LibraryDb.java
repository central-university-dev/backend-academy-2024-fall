package backend.academy.seminar5.project.db;

import backend.academy.seminar5.project.db.entities.Author;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookCategory;
import backend.academy.seminar5.project.db.entities.BookInstance;
import backend.academy.seminar5.project.db.entities.BookInstanceStatus;
import backend.academy.seminar5.project.db.entities.BookRental;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LibraryDb {
    public static final Map<Long, Author> AUTHORS = new HashMap<>(Map.of(
        1L,
        new Author(
            1L,
            "J.R.R. Tolkien",
            "British writer and academic"
        ),

        2L,
        new Author(
            2L,
            "J.K. Rowling",
            "British author, philanthropist"
        )
    ));

    public static final Map<Long, Book> BOOKS = new HashMap<>(Map.of(
        1L,
        Book.builder()
            .id(1L)
            .title("The Lord of the Rings")
            .authors(List.of(AUTHORS.get(1L)))
            .categories(EnumSet.of(BookCategory.ROMANCE, BookCategory.HISTORY))
            .isbn("1234567890")
            .yearPublished(1954)
            .build(),

        2L,
        Book.builder()
            .id(2L)
            .title("Harry Potter and the Philosopher's Stone")
            .authors(List.of(AUTHORS.get(2L)))
            .categories(EnumSet.of(BookCategory.ROMANCE, BookCategory.FANTASY))
            .isbn("1234567891")
            .yearPublished(1997)
            .build()
    ));

    public static final Map<Long, BookInstance> BOOKS_INSTANCES = new HashMap<>(
        Map.of(
            1L, BookInstance.builder().id(1).book(BOOKS.get(1L)).status(BookInstanceStatus.AVAILABLE).build(),
            2L, BookInstance.builder().id(2).book(BOOKS.get(1L)).status(BookInstanceStatus.AVAILABLE).build(),
            3L, BookInstance.builder().id(3).book(BOOKS.get(1L)).status(BookInstanceStatus.AVAILABLE).build(),
            4L, BookInstance.builder().id(4).book(BOOKS.get(1L)).status(BookInstanceStatus.AVAILABLE).build(),
            5L, BookInstance.builder().id(5).book(BOOKS.get(1L)).status(BookInstanceStatus.AVAILABLE).build(),

            6L, BookInstance.builder().id(6).book(BOOKS.get(2L)).status(BookInstanceStatus.AVAILABLE).build(),
            7L, BookInstance.builder().id(7).book(BOOKS.get(2L)).status(BookInstanceStatus.AVAILABLE).build(),
            8L, BookInstance.builder().id(8).book(BOOKS.get(2L)).status(BookInstanceStatus.AVAILABLE).build(),
            9L, BookInstance.builder().id(9).book(BOOKS.get(2L)).status(BookInstanceStatus.AVAILABLE).build()
        )
    );

    public static final Map<Long, BookRental> BOOKS_RENTALS;

    static {
        long id1 = BookRental.nextId();
        long id2 = BookRental.nextId();
        LocalDateTime now = LocalDateTime.now();

        BOOKS_RENTALS = new HashMap<>(
            Map.of(
                id1,
                BookRental.builder()
                    .id(id1)
                    .user(UserDb.getUserById(1L))
                    .bookInstance(BOOKS_INSTANCES.get(1L))
                    .bookingDateTime(now)
//                    .returningDateTime(now)
                    .dueDateTime(now.plusDays(14))
                    .build()
            )
        );
    }
}


