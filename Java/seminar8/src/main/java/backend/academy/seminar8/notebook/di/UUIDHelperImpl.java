package backend.academy.seminar8.notebook.di;

import java.util.UUID;

public class UUIDHelperImpl implements UUIDHelper {
    @Override
    public UUID randomUUID() {
        return UUID.randomUUID();
    }
}
