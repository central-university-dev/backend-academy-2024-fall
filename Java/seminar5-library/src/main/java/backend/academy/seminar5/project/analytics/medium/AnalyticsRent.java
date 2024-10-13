package backend.academy.seminar5.project.analytics.medium;

import backend.academy.seminar5.project.db.LibraryDb;
import java.time.Duration;
import java.time.LocalDateTime;

public class AnalyticsRent {
    void averageTimeOfRental() {
        LibraryDb.BOOKS_RENTALS.values().stream()
            .mapToLong(rental -> Duration.between(
                rental.getBookingDateTime(),
                rental.getReturningDateTime() != null
                    ? rental.getReturningDateTime()
                    : LocalDateTime.now()).toDays())
            .average()
            .orElse(0.0);
    }
}
