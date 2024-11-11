package backend.academy.seminar10.refactor.rename;

// переименовать поле/класс/файл/пакет SHIFT + F6

public class Rename {
    private String goodName;

    public Rename(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }
}
