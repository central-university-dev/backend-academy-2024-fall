package backend.academy.seminar12.domain.events;

import backend.academy.seminar12.domain.ResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;


@UtilityClass
public class EventUtils {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper()
                    .configure(IGNORE_UNKNOWN, true);

    private static final int DEFAULT_PAGE_SIZE = 20;

    public static final ResponseMapper<String, Events> EVENTS_MAPPER =
            json -> OBJECT_MAPPER.readValue(json, Events.class);

    public static List<EnrichedEvent> getEvents(int total) {
        return Stream.iterate(0, i -> i + 1)
                .map(page -> doGetEventsPage(page + 1))
                .map(EVENTS_MAPPER)
                .map(Events::events)
                .flatMap(Collection::stream)
                .map(EnrichedEvent::new)
                .limit(total)
                .toList();
    }

    public static Stream<EnrichedEvent> getEvents() {
        return Stream.iterate(0, i -> i + 1)
                .map(page -> doGetEventsPage(page + 1))
                .map(EVENTS_MAPPER)
                .map(Events::events)
                .flatMap(Collection::stream)
                .map(EnrichedEvent::new);
    }

    @SneakyThrows
    public static List<EnrichedEvent> getEventsPage(int page) {
        return EVENTS_MAPPER.map(doGetEventsPage(page)).events().stream()
                .map(EnrichedEvent::new)
                .toList();
    }

    @SneakyThrows
    private static String doGetEventsPage(int page) {
        @SuppressWarnings("deprecation")
        var eventsApi = new URL(
                "https://kudago.com/public-api/v1.2/events/?" +
                        "page_size=" + DEFAULT_PAGE_SIZE + "&page=" + page +
                        "&fields=id,title,favorites_count,comments_count&location=spb");

        var connection = (HttpURLConnection) eventsApi.openConnection();
        try (var in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            var response = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

}
