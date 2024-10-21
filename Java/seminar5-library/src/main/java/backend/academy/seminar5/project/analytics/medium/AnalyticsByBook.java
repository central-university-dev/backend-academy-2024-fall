package backend.academy.seminar5.project.analytics.medium;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Author;
import backend.academy.seminar5.project.db.entities.BookInstance;
import backend.academy.seminar5.project.db.entities.BookRental;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnalyticsByBook {
    //авторы, чьи книги никогда не брали
    Set<Author> authorsWhoseBooksWasNeverRented() {
        return LibraryDb.BOOKS.values().stream()
            .filter(book -> LibraryDb.BOOKS_INSTANCES.values().stream()
                .noneMatch(instance -> instance.getBook().equals(book)
                                       && LibraryDb.BOOKS_RENTALS.values().stream()
                                           .anyMatch(rental -> rental.getBookInstance().equals(instance))))
            .flatMap(book -> book.getAuthors().stream())
            .collect(Collectors.toSet());
    }

    //книги, которые сейчас на руках
    List<BookInstance> currentlyRentedBooks() {
        return LibraryDb.BOOKS_RENTALS.values().stream()
            .filter(rental -> rental.getReturningDateTime() == null)
            .map(BookRental::getBookInstance)
            .distinct()
            .toList();
    }
}
