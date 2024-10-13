package backend.academy.seminar5.project.service;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.UserDb;
import backend.academy.seminar5.project.db.entities.BookInstance;
import backend.academy.seminar5.project.db.entities.BookInstanceStatus;
import backend.academy.seminar5.project.db.entities.BookRental;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookRentalService {
    public BookRental getBookRental(long rentalId) {
        BookRental bookRental = LibraryDb.BOOKS_RENTALS.get(rentalId);
        if (bookRental == null) {
            throw new RuntimeException("BookRental not found by id " + rentalId);
        } else {
            return bookRental;
        }
    }

    public BookRental rentBook(long bookId, long userId) {
        BookInstance bookInstance = LibraryDb.BOOKS_INSTANCES.get(bookId);
        if (bookInstance == null) {
            throw new RuntimeException("BookRental not found by id: " + bookId + " userId:" + userId);
        }

        bookInstance.setStatus(BookInstanceStatus.BOOKED);

        BookRental newBookRental = buildNewBookRental(bookInstance, userId);
        LibraryDb.BOOKS_RENTALS.put(newBookRental.getId(), newBookRental);

        return newBookRental;
    }

    public void returnBook(long rentalId) {
        BookRental bookRental = getBookRental(rentalId);
        bookRental.setReturningDateTime(LocalDateTime.now());
        bookRental.getBookInstance().setStatus(BookInstanceStatus.AVAILABLE);
    }

    //stream.
    public List<BookRental> rentBooks(List<Long> booksId, long userId) {
        return booksId.stream()
            .map(id -> rentBook(id, userId))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private BookRental buildNewBookRental(BookInstance bookInstance, long userId) {
        final LocalDateTime now = LocalDateTime.now();
        bookInstance.setStatus(BookInstanceStatus.RESERVED);

        return BookRental.builder()
            .id(BookRental.nextId())
            .user(UserDb.getUserById(userId))
            .bookInstance(bookInstance)
            .bookingDateTime(now)
            .dueDateTime(now.plus(Duration.ofDays(14)))
            .build();
    }
}
