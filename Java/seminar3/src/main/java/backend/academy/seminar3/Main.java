package backend.academy.seminar3;

import backend.academy.seminar3.practice.solution.Result;
import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    void main() {
        var user1 = new User("Anton", 17);
        var user2 = new User("Mariya", 18);

        var ticketResult1 = user1.takeTicket();
        var ticketResult2 = user2.takeTicket();

        System.out.println(STR."1) Is success for Anton: \{ticketResult1.isSuccess()}");
        System.out.println(STR."2) Is success for Mariya: \{ticketResult2.isSuccess()}\n");

        ticketResult1.ifFailure(
            (message) -> {
                System.out.println(STR."1) Error happend while taking a ticket for Anton: \{message}");
            }
        );

        ticketResult2.ifSuccess(
            (ticket) -> {
                System.out.println(STR."2) Taking a ticket for Mariya completed successfully: \{ticket}");
            }
        );
    }

    public record User(String name, int age) {
        public Result<? extends Ticket> takeTicket() {
            if (age < 18) {
                return new Result.Failure<>("Age must be at least 18");
            }

            return new Result.Success<>(
                new Ticket(UUID.randomUUID(), LocalDateTime.now())
            );
        }
    }

    public record Ticket(UUID id, LocalDateTime timestamp) { }
}
