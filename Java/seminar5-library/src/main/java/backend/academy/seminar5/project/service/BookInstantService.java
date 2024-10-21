package backend.academy.seminar5.project.service;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookInstance;
import java.util.ArrayList;
import java.util.List;

public class BookInstantService {
    public BookInstance findBookInstanceById(long bookInstanceId) {
        BookInstance bookInstance = LibraryDb.BOOKS_INSTANCES.get(bookInstanceId);
        if (bookInstance == null) {
            throw new RuntimeException("BookInstance with id " + bookInstanceId + " not found");
        } else {
            return bookInstance;
        }
    }

    /**
     * Пополнение книг
     */
    public void addBookInstance(List<BookInstance> bookInstance) {
        for (BookInstance instance : bookInstance) {
            LibraryDb.BOOKS_INSTANCES.put(instance.getId(), instance);
        }

        //или
        bookInstance.forEach(instance -> LibraryDb.BOOKS_INSTANCES.put(instance.getId(), instance));
    }

    public List<Book> countLostBookInstance() {
        final List<Book> lostBooks = new ArrayList<>();

        for (var bookRent : LibraryDb.BOOKS_RENTALS.values()) {
            if (bookRent.getReturningDateTime() == null) {
                lostBooks.add(bookRent.getBookInstance().getBook());
            }
        }

        return lostBooks;
    }
}
