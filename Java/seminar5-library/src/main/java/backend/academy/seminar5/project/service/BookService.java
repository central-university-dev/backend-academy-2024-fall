package backend.academy.seminar5.project.service;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Book;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookService {

    // Поиск всех экземпляров книги по названию книги
    public List<String> findAllTitlesContaining(String title) {
        final ArrayList<Book> filteredBooks = new ArrayList<>();

        LibraryDb.BOOKS.forEach((key, book) -> { //! или for(el:collection){...} или iterator
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                filteredBooks.add(book);
            }
        });

        //еще один из способов итерироваться
        List<String> titles = new ArrayList<>();
        Iterator<Book> iterator = filteredBooks.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            titles.add(book.getTitle());

            //плюс работы через iterator - можно удалять элементы из коллекции, а через for-each - нельзя
            if (false) {
                iterator.remove();
            }
        }
        return titles;
    }
}
