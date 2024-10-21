package backend.academy.seminar5.project.service;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Author;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookCategory;
import backend.academy.seminar5.project.db.entities.BookInstance;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LibrarySearchService {
    public List<Book> getBooksByAuthor(String authorName) {
        Set<Map.Entry<Long, Book>> entries = LibraryDb.BOOKS.entrySet();
        Iterator<Map.Entry<Long, Book>> iterator = entries.iterator();

        List<Book> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Long, Book> nextEntry = iterator.next();
            Long bookId = nextEntry.getKey();
            Book book = nextEntry.getValue();

            List<Author> authors = book.getAuthors();
            for (Author author : authors) {
                if (author.getName().contains(authorName)) {
                    result.add(book);
                }
            }
        }

        return result;
    }

    public List<Book> allBooksByYear(int year) {
        return LibraryDb.BOOKS.values().stream()
            .filter(book -> book.getYearPublished() > year)
//            .collect(Collectors.toUnmodifiableList());
//            .collect(Collectors.toList())
            .toList();
    }

    public List<Book> allBooksByCategory(BookCategory bookCategory) {
        return LibraryDb.BOOKS.values().stream()
            .filter(book -> book.getCategories().contains(bookCategory))
            .toList();
    }

    public Map<Author, Book> allBooksByAuthor(String authorName) {
        //todo:
        return Map.of();
    }

    public Optional<Book> mostPopularBook() {
        return LibraryDb.BOOKS_INSTANCES.values().stream()
            .max(Comparator.comparingInt(BookInstance::getTimesRented))
            .map(BookInstance::getBook);
    }
}
