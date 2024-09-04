package backend.academy.cars;

import backend.academy.interfaces.Fuelable;
import backend.academy.interfaces.Infotainment;
import backend.academy.interfaces.Startable;

public abstract class Car
    implements Fuelable, Startable, Infotainment {
    // Поля (атрибуты) класса
    public String make;       // Производитель
    public String model;      // Модель
    public int year;          // Год выпуска
    public String color;      // Цвет
    public double fuelLevel;  // Уровень топлива
    public boolean isEngineOn; // Состояние двигателя

    // Конструктор
    public Car(String make, String model, int year, String color) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.fuelLevel = 100.0; // Предположим, полный бак при создании
        this.isEngineOn = false; // Двигатель выключен при создании
    }
    // Реализация методов Fuelable
    @Override
    public void refuel(double amount) {
        fuelLevel += amount;
        System.out.println("Added " + amount + " liters of fuel. Total fuel: " + fuelLevel + " liters.");
    }

    @Override
    public double checkFuelLevel() {
        return fuelLevel;
    }

    // Реализация методов Startable
    @Override
    public void startEngine() {
        if (!isEngineOn) {
            isEngineOn = true;
            System.out.println("Engine started.");
        } else {
            System.out.println("Engine is already on.");
        }
    }

    @Override
    public void stopEngine() {
        if (isEngineOn) {
            isEngineOn = false;
            System.out.println("Engine stopped.");
        } else {
            System.out.println("Engine is already off.");
        }
    }

    // Реализация методов Infotainment
    @Override
    public void playMusic(String trackName) {
        System.out.println("Playing track: " + trackName);
    }

    @Override
    public void displayNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
    }
}
