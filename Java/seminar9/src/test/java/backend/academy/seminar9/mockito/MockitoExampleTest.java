package backend.academy.seminar9.mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MockitoExampleTest {

    private static class ProductService {
        private final Map<Integer, String> map = new HashMap<>();

        public String get(Integer key) {
            return map.get(key);
        }

        public void put(int key, String value) {
            map.put(key, value);
        }
    }

    @Test
    void mockingExampleTest() {
        ProductService mockService = mock(ProductService.class);

        // By default, methods of mocked service do nothing,
        // and returns default values (null, 0, false, etc.)
        mockService.put(23, "some val");
        assertNull(mockService.get(23));

        when(mockService.get(23)).thenReturn("Mocked value!");

        String value = mockService.get(23);
        assertEquals("Mocked value!", value);

        verify(mockService, times(2)).get(23);
    }

    @Test
    void mockitoMoreExamples() {
        ProductService mockService = mock(ProductService.class);

        when(mockService.get(0)).thenReturn(null);
        assertNull(mockService.get(0));

        when(mockService.get(anyInt())).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> mockService.get(653636));

        // special consideration for void methods
        doThrow(IllegalStateException.class).when(mockService).put(50, "void");
        assertThrows(IllegalStateException.class, () -> mockService.put(50, "void"));
    }

    @Test
    void spyExamples() {
        AtomicInteger counter = new AtomicInteger(0);
        AtomicInteger spy = spy(counter);

        when(spy.get()).thenReturn(-1);

        assertEquals(1, spy.incrementAndGet());
        assertEquals(2, spy.incrementAndGet());

        assertEquals(-1, spy.get());

        assertEquals(3, spy.incrementAndGet());

        // "spy" is a copy! Original object not aware of changes
        assertEquals(0, counter.get());

        AtomicInteger mock = mock(AtomicInteger.class);

        // always the same result for "mock"
        assertEquals(0, mock.incrementAndGet());
    }
}
