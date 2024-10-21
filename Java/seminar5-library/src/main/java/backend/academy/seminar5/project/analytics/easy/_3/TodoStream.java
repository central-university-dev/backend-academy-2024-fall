package backend.academy.seminar5.project.analytics.easy._3;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookInstanceStatus;
import java.util.List;
import java.util.Map;

public class TodoStream {
    List<Book> allBooksThatFromAuthorAndCategory() {
        //todo:
        Map<Long, Book> books = LibraryDb.BOOKS;
        return null;
    }

    List<String> allBookInstanceStatusesAsc() {
        //todo:
        BookInstanceStatus[] statuses = BookInstanceStatus.values();
        return null;
    }
}
