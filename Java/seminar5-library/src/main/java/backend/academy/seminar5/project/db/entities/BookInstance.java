package backend.academy.seminar5.project.db.entities;

import lombok.Builder;
import lombok.Data;

/**
 * Реальный экземпляр книги
 * {@link BookInstanceStatus} - может находиться у кого-то, а может на полке
 */
@Data
@Builder
public class BookInstance {
    private long id;
    private Book book;
    private BookInstanceStatus status;

    private int timesRented;

    // Конструкторы, геттеры и сеттеры будут автоматически сгенерированы благодаря Lombok
}
