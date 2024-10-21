package backend.academy.seminar5.project.service;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookCategory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Метод возвращает данные о наиболее популярных категориях книг с учётом определённого автора (или авторов)
// и о самых популярных книгах в каждой из этих категорий.
public class LibraryAnalyticsService {
    // todo: DEBUG IT
    // SHOW - IDEA DEBUGGER FOR STREAMS
    public Map<BookCategory, List<Book>> getTopCategoriesWithPopularBooks(
        String authorNamePattern,
        int topCategoriesCount,
        int limitInCategory
    ) {
        return LibraryDb.BOOKS.values()
            .stream()
            .filter(book -> book.getAuthors().stream().anyMatch(author -> author.getName().matches(authorNamePattern)))
            .flatMap(book -> book.getCategories().stream())
            .collect(Collectors.groupingBy(category -> category, Collectors.counting()))
            .entrySet()

            .stream()
            .sorted(Map.Entry.<BookCategory, Long>comparingByValue().reversed())
            .limit(topCategoriesCount)
            .map(Map.Entry::getKey)
            .toList()

            .stream()
            .collect(Collectors.toMap(
                category -> category,
                category -> LibraryDb.BOOKS.values().stream()
                    .filter(book -> book.getCategories().contains(category))
                    .collect(Collectors.toMap(
                        book -> book,
                        book -> LibraryDb.BOOKS_RENTALS.values().stream()
                            .filter(rental -> rental.getBookInstance().getBook().getId() == book.getId())
                            .count()
                    ))
                    .entrySet()

                    .stream()
                    .sorted(Map.Entry.<Book, Long>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .limit(limitInCategory)
                    .toList()
            ));
    }
}
