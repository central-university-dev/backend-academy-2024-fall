package backend.academy.seminar5.project.analytics.dto;

import backend.academy.seminar5.project.db.entities.Author;
import backend.academy.seminar5.project.db.entities.Book;
import lombok.Data;

@Data
public class AuthorStatistics {
    private Author author;

    private int totalBooks;
    private int totalRents;

    private Book mostPopularBook;
    private int maxRents;

    public void addBook(Book book, int rents) {
        totalBooks++;
        totalRents += rents;
        if (rents > maxRents) {
            mostPopularBook = book;
            maxRents = rents;
        }
    }

    public void merge(AuthorStatistics other) {
        totalBooks += other.totalBooks;
        totalRents += other.totalRents;

        if (other.maxRents > maxRents) {
            mostPopularBook = other.mostPopularBook;
            maxRents = other.maxRents;
        }
    }

}
