package backend.academy.seminar10.refactor.extract;


// число 0.0001 - дельта для сравнения
// нужно вывести дельту в параметр метода
// навести курсор на число, нажать Ctrl+Alt+P / Cmd+Option+P

public class ExtractParam {

    boolean testMethod() {
        return isEquals(0.1, 0.1)
               && isEquals(0.2, 0.2);
    }

    boolean isEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0001;
    }
}
