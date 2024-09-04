import backend.academy.cars.Car;
import backend.academy.cars.ElectricCar;
import backend.academy.cars.GasCar;

public static void main() {
    // Создаем массив автомобилей
    Car[] cars = {new ElectricCar(), new ElectricCar(), new GasCar()};

    // Используем полиморфизм для вызова метода startEngine
    for (Car car : cars) {
        car.startEngine();
    }
}
