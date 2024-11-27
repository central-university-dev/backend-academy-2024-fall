package backend.academy.seminar12.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Events(@JsonProperty("results") List<Event> events) {

    @JsonCreator
    public Events {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Event(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("comments_count") int comments,
            @JsonProperty("favorites_count") int favorites) {

        @JsonCreator
        public Event {
        }

    }

}
