package backend.academy.chess.figure.impl;

import backend.academy.chess.figure.AbstractFigure;
import backend.academy.chess.figure.Color;
import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;

public class King extends AbstractFigure {
    public King(Color colorParam) {
        super(colorParam);
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? "k" : "K";
    }

    public void tryAttack(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        tryMove(from, to, field, f1, f2);
    }

    public void tryMove(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        int diffLet = to.letter - from.letter;
        int diffNum = to.number - from.number;

        if (!canMakeMove(diffLet, diffNum)) {
            throw new RuntimeException("can't attack with coords " + diffLet + " " + diffNum);
        }
    }

    private static boolean canMakeMove(int diffLet, int diffNum) {
        return ((diffLet == -1 || diffLet == 1) && (diffNum == -1 || diffNum == 0 || diffNum == 1))
               || (diffLet == 0 && (diffNum == 1 || diffNum == -1));
    }
}
