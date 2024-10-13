package backend.academy.seminar5.project.analytics.medium;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookInstance;
import backend.academy.seminar5.project.db.entities.BookRental;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnalyticsByUser {
    //книги, которые брал пользователь
    public List<Book> booksRentedByUser(long userId) {
        return LibraryDb.BOOKS_RENTALS.values().stream()
            .filter(rental -> rental.getUser().getId() == userId)
            .map(rental -> rental.getBookInstance().getBook())
            .distinct()
            .toList();
    }

    //книги, которые брал пользователь со счетчиком
    public Map<Book, Long> booksRentedByUserWithCount(long userId) {
        Map<Book, Long> collectedBooks = LibraryDb.BOOKS_RENTALS.values().stream()
            .filter(rental -> rental.getUser().getId() == userId) // Фильтрация аренды для конкретного пользователя
            .map(BookRental::getBookInstance)
            .map(BookInstance::getBook) // Преобразование экземпляра книги в книгу
            .distinct()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        //
        return collectedBooks.entrySet().stream()
            .sorted(Map.Entry.<Book, Long>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }
}
