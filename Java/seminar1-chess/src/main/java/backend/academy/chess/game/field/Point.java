package backend.academy.chess.game.field;

public class Point {
    public int letter;
    public int number;

    public Point(int number, int letter) {
        this.letter = letter;
        this.number = number;
    }

    @Override
    public String toString() {
        return "[" + number + ":" + letter + "]";
    }

    public Point calcAll(Point to) {
        System.out.println("Trying find move vector...");
        try {
            System.out.println("diag?");
            return calcDiagMovePoint(to);
        } catch (RuntimeException e) {
            System.out.println("no..." + e.getMessage());
            System.out.println("strg?");
            return calcStraightMovePoint(to);
        }
    }

    public Point calcDiagMovePoint(Point to) {
        Point result;
        if (number < 0 && letter < 0) {
            System.out.println("up - left");
            result = new Point(to.number + 1, to.letter + 1);

        } else if (number < 0 && letter > 0) {
            System.out.println("up - right");
            result = new Point(to.number + 1, to.letter - 1);

        } else if (number > 0 && letter < 0) {
            System.out.println("down - left");
            result = new Point(to.number - 1, to.letter + 1);

        } else if (number > 0 && letter > 0) {
            System.out.println("down - right");
            result = new Point(to.number - 1, to.letter - 1);
        } else {
            throw new RuntimeException("can't calc diag");
        }

        System.out.println("diag point to stop: " + result + " - " + to);
        return result;
    }

    public Point calcStraightMovePoint(Point to) {
        Point result;
        if (number < 0) {
            System.out.println("up");
            result = new Point(to.number + 1, to.letter);

        } else if (number > 0) {
            System.out.println("down");
            result = new Point(to.number - 1, to.letter);

        } else if (letter > 0) {
            System.out.println("left");
            result = new Point(to.number, to.letter - 1);

        } else if (letter < 0) {
            System.out.println("right");
            result = new Point(to.number, to.letter + 1);

        } else {
            throw new RuntimeException("can't calc straight");
        }

        System.out.println("strg point to stop: " + result + " - " + to);
        return result;

    }

    public boolean isSame(Point another) {
        return this.number == another.number && this.letter == another.letter;
    }

}
