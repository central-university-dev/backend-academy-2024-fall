package backend.academy.shapes.records;

class ShapeCalculator {

    public double area(Shape shape) {
        return switch (shape) {
            case Circle(double radius) -> Math.PI * radius * radius;
            case Rectangle(double length, double width) -> length * width;
        };
    }

    public double perimeter(Shape shape) {
        if (shape instanceof Circle(double radius)) {
            return 2 * Math.PI * radius;
        } else if (shape instanceof Rectangle(double length, double width)) {
            return length * width;
        } else {
            throw new IllegalArgumentException("Shape is not supported");
        }
    }

    /* Further, any other operation can be added easily */
}
