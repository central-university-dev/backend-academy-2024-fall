package backend.academy.shapes.interfaces;

class CodeSpace {

    interface Shape {
        double area();
        double perimeter();
    }

    record Rectangle(double width, double length) implements Shape {
        @Override
        public double area() {
            return width * length;
        }

        @Override
        public double perimeter() {
            return (width + length) * 2;
        }
    }

    record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }

        @Override
        public double perimeter() {
            return 2 * Math.PI * radius;
        }
    }
}
