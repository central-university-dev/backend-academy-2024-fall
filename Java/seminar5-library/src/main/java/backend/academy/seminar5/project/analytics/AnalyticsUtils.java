package backend.academy.seminar5.project.analytics;

import backend.academy.seminar5.project.analytics.dto.AuthorStatistics;
import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Author;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookInstance;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AnalyticsUtils {
    public static Collection<Author> getAllAuthors() {
        // Подзадача 1: Уникальные авторы
        Set<Author> uniqueAuthors = LibraryDb.BOOKS.values().stream()
            .flatMap(book -> book.getAuthors().stream())
            .collect(Collectors.toSet());

        System.out.println("Уникальные авторы:");
        uniqueAuthors.forEach(author -> System.out.println(author.getName()));

        return uniqueAuthors;
    }

    public static Set<Book> getBooksWithMultipleAuthors() {
        // Подзадача 2: Книги с несколькими авторами
        Set<Book> multiAuthorBooks = LibraryDb.BOOKS.values().stream()
            .filter(book -> book.getAuthors().size() > 1)
            .collect(Collectors.toSet());

        System.out.println("Книги, написанные несколькими авторами:");
        multiAuthorBooks.forEach(System.out::println);

        return multiAuthorBooks;
    }

    public static Map<Author, Long> countBooksPerAuthor() {
        // Подзадача 3: Количество книг для каждого автора
        Map<Author, Long> booksPerAuthor = LibraryDb.BOOKS.values().stream()
            .flatMap(book -> book.getAuthors().stream())
            .collect(Collectors.groupingBy(author -> author, Collectors.counting()));

        System.out.println("Количество книг для каждого автора:");
        booksPerAuthor.forEach((author, count) -> System.out.println(author.getName() + ": " + count));

        return booksPerAuthor;
    }

    public static Map<Author, AuthorStatistics> analyzeLibrary() {
        return LibraryDb.BOOKS_INSTANCES.entrySet().stream()
            .filter(entry -> entry.getValue().getTimesRented() > 0)
            .flatMap(entry -> entry.getValue().getBook().getAuthors().stream()
                .map(author -> new AbstractMap.SimpleEntry<>(author, entry)))
            .collect(
                Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collector.of(
                        AuthorStatistics::new,
                        (stats, entry) -> {
                            Book book = entry.getValue().getValue().getBook();
                            int totalRents = LibraryDb.BOOKS_INSTANCES.values().stream()
                                .mapToInt(BookInstance::getTimesRented)
                                .sum();
                            stats.addBook(book, totalRents);
                        },
                        (stats1, stats2) -> {
                            stats1.merge(stats2);
                            return stats1;
                        }
                    )
                )
            );
    }

}
