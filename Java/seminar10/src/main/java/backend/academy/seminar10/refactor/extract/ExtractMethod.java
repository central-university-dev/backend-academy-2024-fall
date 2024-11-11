package backend.academy.seminar10.refactor.extract;

// расчет переменной v нужно вынести в отдельный метод
// навести курсор на переменную, нажать Ctrl+Alt+M / Cmd+Option+M

public class ExtractMethod {
    String extractMethod() {
        double v = Math.sqrt(1d) + Math.sqrt(2d);
        return String.valueOf(v);
    }

}
