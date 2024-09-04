package backend.academy.chess.figure.impl;

import backend.academy.chess.figure.AbstractFigure;
import backend.academy.chess.figure.Color;
import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;

public class Pawns extends AbstractFigure {
    public Pawns(Color colorParam) {
        super(colorParam);
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? "p" : "P";
    }

    public void tryAttack(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        if (from.letter == to.letter) {
            throw new RuntimeException("Can't attack without let change");
        }

        int dif = f1.getColor() == Color.WHITE ? 1 : -1;
        if (from.number != to.number + dif) {
            throw new RuntimeException("More than 1 line difference");
        }

        if (from.letter != to.letter - 1 && from.letter != to.letter + 1) {
            throw new RuntimeException("Attack without letter change");
        }

    }

    // check if move is possible
    public void tryMove(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2) {
        changeLetterOnly(from, to);

        int rowForHop;
        int waypointDif;

        if (f1.getColor() == Color.WHITE) {
            rowForHop = 6;
            waypointDif = -1;
        } else {
            rowForHop = 1;
            waypointDif = 1;
        }

        int diffNum = Math.abs(to.number - from.number);
        if (diffNum == 1) {
            if (waypointDif != to.number - from.number) {
                throw new RuntimeException("Can't move backwards");
            }
        } else if (diffNum == 2) {
            if (from.number != rowForHop) {
                throw new RuntimeException("You must stay on " + (rowForHop + 1) + " line to do this move");
            }
            if (field.getFigure(new Point(from.number + waypointDif, from.letter)) != null) {
                throw new RuntimeException("Figure on the way");
            }
        } else {
            throw new RuntimeException("Wrong move length");
        }
    }

    private void changeLetterOnly(Point from, Point to) {
        if (from.letter != to.letter) {
            throw new RuntimeException("Can't change letter while move");
        }
    }

}
