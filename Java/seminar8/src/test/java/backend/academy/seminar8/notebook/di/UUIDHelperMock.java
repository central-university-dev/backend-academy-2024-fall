package backend.academy.seminar8.notebook.di;

import java.util.UUID;

public class UUIDHelperMock implements UUIDHelper {
    private final UUID uuid;

    public UUIDHelperMock(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID randomUUID() {
        return uuid;
    }
}
