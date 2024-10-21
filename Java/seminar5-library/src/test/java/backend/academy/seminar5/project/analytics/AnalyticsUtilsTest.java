package backend.academy.seminar5.project.analytics;

import org.junit.jupiter.api.Test;

class AnalyticsUtilsTest {

    @Test
    void getAllAuthors() {
        AnalyticsUtils.getAllAuthors();
    }

    @Test
    void getBooksWithMultipleAuthors() {
        AnalyticsUtils.getBooksWithMultipleAuthors();
    }

    @Test
    void countBooksPerAuthor() {
        AnalyticsUtils.countBooksPerAuthor();
    }
}
