package backend.academy.seminar10.refactor.rename;

// Поменять тип поля
// Навести курсор на поле, Ctrl+Shift+F6 или Cmd+Shift+F6

public class ChangeType {
    Integer s1;
    int i1;

    public ChangeType(Integer s1, int i1) {
        this.s1 = s1;
        this.i1 = i1;
    }

    public Integer getS1() {
        return s1;
    }

    public void setS1(Integer s1) {
        this.s1 = s1;
    }
}

