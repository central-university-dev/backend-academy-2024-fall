package backend.academy.seminar8.car;

interface Engine {
    boolean isStart();
}

class CarEngine implements Engine {
    @Override
    public boolean isStart() {
        return true;
    }
}

class Car {

    private CarEngine engine = new CarEngine();

//    public Car(Engine engine) {
//        this.engine = engine;
//    }

    public void start() {
        if (engine.isStart()) {
            System.out.println("Start!");
        }
    }

    public static void main(String[] args) {
        new Car().start();
//        new Car(new CarEngine()).start();
    }
}

