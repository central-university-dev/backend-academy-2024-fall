package backend.academy.seminar5.project.service;

import backend.academy.seminar5.project.db.LibraryDb;
import backend.academy.seminar5.project.db.entities.Author;
import backend.academy.seminar5.project.db.entities.Book;
import backend.academy.seminar5.project.db.entities.BookCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Метод возвращает данные о наиболее популярных категориях книг с учётом определённого автора (или авторов)
// и о самых популярных книгах в каждой из этих категорий.
public class LibraryAnalyticsService2 {

    //более простой и правильный вариант без стримов
    public Map<BookCategory, List<Book>> getTopCategoriesWithPopularBooks2(
        String authorNamePattern,
        int topCategoriesCount,
        int limitInCategory
    ) {
        // 1. Фильтр книг по авторам
        List<Book> filteredBooks = filterBooksByAuthor(authorNamePattern);

        // 2. Подсчет книг по категориям
        Map<BookCategory, Long> categoryCounts = countBooksByCategory(filteredBooks);

        // 3. Определение популярных категорий
        List<BookCategory> topCategories = getTopCategories(categoryCounts, topCategoriesCount);

        // 4. Для каждой топовой категории определим популярные книги
        Map<BookCategory, List<Book>> result = new HashMap<>();
        for (BookCategory category : topCategories) {
            List<Book> popularBooks = getPopularBooksInCategory(category, limitInCategory);
            result.put(category, popularBooks);
        }

        return result;
    }

    private List<Book> filterBooksByAuthor(String authorNamePattern) {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : LibraryDb.BOOKS.values()) {
            for (Author author : book.getAuthors()) {
                if (author.getName().matches(authorNamePattern)) {
                    matchingBooks.add(book);
                    break;
                }
            }
        }
        return matchingBooks;
    }

    private Map<BookCategory, Long> countBooksByCategory(List<Book> books) {
        Map<BookCategory, Long> categoryCounts = new HashMap<>();
        for (Book book : books) {
            for (BookCategory category : book.getCategories()) {
                categoryCounts.put(category, categoryCounts.getOrDefault(category, 0L) + 1);
            }
        }
        return categoryCounts;
    }

    private List<BookCategory> getTopCategories(
        Map<BookCategory, Long> categoryCounts,
        int topCategoriesCount
    ) {
        List<BookCategory> categories = new ArrayList<>(categoryCounts.keySet());
        categories.sort((c1, c2) -> categoryCounts.get(c2).compareTo(categoryCounts.get(c1)));
        return categories.subList(0, Math.min(topCategoriesCount, categories.size()));
    }

    private List<Book> getPopularBooksInCategory(BookCategory category, int limitInCategory) {
        Map<Book, Long> bookPopularity = new HashMap<>();

        for (Book book : LibraryDb.BOOKS.values()) {
            if (book.getCategories().contains(category)) {
                long rentalCount = 0;
                for (var rental : LibraryDb.BOOKS_RENTALS.values()) {
                    if (rental.getBookInstance().getBook().getId() == book.getId()) {
                        rentalCount++;
                    }
                }
                bookPopularity.put(book, rentalCount);
            }
        }

        List<Book> popularBooks = new ArrayList<>(bookPopularity.keySet());
        popularBooks.sort((b1, b2) -> bookPopularity.get(b2).compareTo(bookPopularity.get(b1)));
        return popularBooks.subList(0, Math.min(limitInCategory, popularBooks.size()));
    }

}
