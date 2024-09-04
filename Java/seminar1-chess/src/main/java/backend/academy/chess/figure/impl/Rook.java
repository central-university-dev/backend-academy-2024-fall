package backend.academy.chess.figure.impl;

import backend.academy.chess.figure.AbstractFigure;
import backend.academy.chess.figure.Color;
import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;

public class Rook extends AbstractFigure {
    public Rook(Color colorParam) {
        super(colorParam);
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? "r" : "R";
    }

    public void tryAttack(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        Point vector = new Point(to.number - from.number, to.letter - from.letter);

        if (vector.number != 0 && vector.letter != 0) {
            throw new RuntimeException("Incorrect move. Rook cant move with vector: " + vector);
        }

        Point[] fromTo = {from, to};
        moveAndAttack(fromTo, field, f1, f2, vector);
    }

    public void tryMove(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        int diffNum = 0;
        int diffLet = 0;

        Point vector = new Point(to.number - from.number, to.letter - from.letter);
        if (vector.number == 0 && vector.letter != 0) {
            diffLet = to.letter - from.letter;
            diffLet = diffLet < 0 ? -1 : 1;
        } else if (vector.number != 0 && vector.letter == 0) {
            diffNum = to.number - from.number;
            diffNum = diffNum < 0 ? -1 : 1;
        } else {
            throw new RuntimeException("Incorrect move. Rook cant move with vector: " + vector);
        }

        field.canGoThrough(from, to, diffNum, diffLet);
    }
}
