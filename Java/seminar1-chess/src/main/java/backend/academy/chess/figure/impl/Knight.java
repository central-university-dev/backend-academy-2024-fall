package backend.academy.chess.figure.impl;

import backend.academy.chess.figure.AbstractFigure;
import backend.academy.chess.figure.Color;
import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;

public class Knight extends AbstractFigure {
    public Knight(Color colorParam) {
        super(colorParam);
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? "n" : "N";
    }

    public void tryAttack(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        tryMove(from, to, field, f1, f2);
    }

    public void tryMove(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        int diffLet = to.letter - from.letter;
        int diffNum = to.number - from.number;

        if ((Math.abs(diffLet) == 2 && (diffNum == -1 || diffNum == 1))
            || (Math.abs(diffLet) == 1 && (diffNum == -2 || diffNum == 2))) {
            // do nothing
        } else {
            throw new RuntimeException("can't attack with coords " + diffLet + " " + diffNum);
        }
    }
}
