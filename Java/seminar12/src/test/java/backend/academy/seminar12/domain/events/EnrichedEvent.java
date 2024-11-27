package backend.academy.seminar12.domain.events;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;


public record EnrichedEvent(
        int id, String title,
        int comments, int favorites,
        LocalDate date, String place, int price) {

    private static final Random RANDOM = new Random();

    private static final List<String> PLACES =
            List.of("Эрмитаж", "Зимний дворец", "Петропавловская крепость", "Петергоф", "Исаакиевский собор",
                    "Часовня Ильи Муромца на Ладожском озере", "Петербургский метрополитен", "Петергофский парк",
                    "Крестовоздвиженский собор", "Летний сад");

    public EnrichedEvent(Events.Event event) {
        this(
                event.id(), event.title(), event.comments(), event.favorites(),
                LocalDate.now().plusDays(RANDOM.nextInt(0, 14)),
                PLACES.get(RANDOM.nextInt(0, PLACES.size())),
                RANDOM.nextInt(0, 1000));
    }

}
