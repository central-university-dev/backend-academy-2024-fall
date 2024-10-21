package backend.academy.seminar8.notebook.di;

import java.time.Instant;

public class TimeHelperImpl implements TimeHelper {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
