package backend.academy.seminar10.refactor.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Преобразовать класс в record
// Alt/Option + Enter, Convert to record class

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ClassWithLombok {
    private final String s1;
    private final String s2;
    private final int i1;
    private final int i2;
}

