package backend.academy.seminar8.notebook.di;

import java.time.Instant;

public class TimeHelperMock implements TimeHelper {
    final Instant now;

    public TimeHelperMock(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }
}
