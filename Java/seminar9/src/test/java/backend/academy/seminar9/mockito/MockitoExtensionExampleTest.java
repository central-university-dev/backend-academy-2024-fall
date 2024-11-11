package backend.academy.seminar9.mockito;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MockitoExtensionExampleTest {

    @Spy
    AtomicInteger spy = new AtomicInteger(0);

    @Mock
    AtomicInteger mock;

    @Test
    void test() {
        when(spy.get()).thenReturn(100);
        when(mock.get()).thenReturn(101);

        assertEquals(1, spy.incrementAndGet());
        assertEquals(100, spy.get());

        assertEquals(0, mock.incrementAndGet());
        assertEquals(101, mock.get());
    }
}
