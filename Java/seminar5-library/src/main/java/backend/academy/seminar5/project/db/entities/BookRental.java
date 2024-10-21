package backend.academy.seminar5.project.db.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookRental {
    private static long lastId = 1;

    private long id;
    private User user;
    private BookInstance bookInstance;
    private LocalDateTime bookingDateTime;
    private LocalDateTime returningDateTime;
    private LocalDateTime dueDateTime;

    public static long nextId() {
        return lastId++;
    }
}
