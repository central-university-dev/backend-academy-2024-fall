package backend.academy.chess.figure;

import backend.academy.chess.figure.impl.Bishop;
import backend.academy.chess.figure.impl.King;
import backend.academy.chess.figure.impl.Knight;
import backend.academy.chess.figure.impl.Pawns;
import backend.academy.chess.figure.impl.Queen;
import backend.academy.chess.figure.impl.Rook;
import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;

public abstract class AbstractFigure implements FigureActions {
    protected Color color;

    public AbstractFigure(Color colorParam) {
        this.color = colorParam;
    }

    public static AbstractFigure create(char figure) {
        return create(String.valueOf(figure));
    }

    public static AbstractFigure create(String figure) {
        switch (figure) {
            case "K":
                return new King(Color.WHITE);
            case "k":
                return new King(Color.BLACK);

            case "Q":
                return new Queen(Color.WHITE);
            case "q":
                return new Queen(Color.BLACK);

            case "B":
                return new Bishop(Color.WHITE);
            case "b":
                return new Bishop(Color.BLACK);

            case "N":
                return new Knight(Color.WHITE);
            case "n":
                return new Knight(Color.BLACK);

            case "R":
                return new Rook(Color.WHITE);
            case "r":
                return new Rook(Color.BLACK);

            case "P":
                return new Pawns(Color.WHITE);
            case "p":
                return new Pawns(Color.BLACK);
            default:
                throw new RuntimeException("unknown figure");
        }
    }

    public Color getColor() {
        return color;
    }

    public void inverseColor() {
        color = color.inverse();
    }

    public void move(Point from, Point to, Field field) {
        if (from.isSame(to)) {
            throw new RuntimeException("move to same point");
        }

        AbstractFigure f1 = field.getFigure(from);
        AbstractFigure f2 = field.getFigure(to);

        if (f2 == null) {
            tryMove(from, to, field, f1, f2);
        } else {
            if (f1.getColor() == f2.getColor()) {
                throw new RuntimeException("you can't attack figure with same color");
            }
            tryAttack(from, to, field, f1, f2);
        }
    }

    protected void moveAndAttack(
        Point[] fromTo,
        Field field,
        AbstractFigure f1,
        AbstractFigure f2,
        Point vector
    ) {
        Point pointWhereToStop = vector.calcAll(fromTo[1]);
        if (!fromTo[0].isSame(pointWhereToStop)) {
            tryMove(fromTo[0], pointWhereToStop, field, f1, f2);
        }
    }

}
