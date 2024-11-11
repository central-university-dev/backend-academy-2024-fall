package backend.academy.seminar10.refactor.record;

// Преобразовать record в обычный класс
// Alt/Option + Enter, Convert record to class

public record NotRecord(
        String s1,
        String s2,
        int i1,
        int i2) {
}


class SomeDtoService {
    void method() {
        NotRecord notRecord = new NotRecord("", "", 1, 2);
        System.out.println(notRecord.s1());
        System.out.println(notRecord.i1());
    }
}
