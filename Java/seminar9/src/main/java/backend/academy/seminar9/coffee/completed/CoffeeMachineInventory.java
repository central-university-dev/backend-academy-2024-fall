package backend.academy.seminar9.coffee.completed;

public class CoffeeMachineInventory {
    private int beans;
    private int water;
    private int milk;

    public CoffeeMachineInventory(int beans, int water, int milk) {
        this.beans = beans;
        this.water = water;
        this.milk = milk;
    }

    public int getBeans() {
        return beans;
    }

    public int getWater() {
        return water;
    }

    public int getMilk() {
        return milk;
    }

    public void setBeans(int beans) {
        this.beans = beans;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    @Override public String toString() {
        return "CoffeeMachineInventory{" +
            "beans=" + beans +
            ", water=" + water +
            ", milk=" + milk +
            '}';
    }
}
