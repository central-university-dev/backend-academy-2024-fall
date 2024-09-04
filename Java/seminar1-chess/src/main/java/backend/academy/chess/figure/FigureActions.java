package backend.academy.chess.figure;

import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;

public interface FigureActions {
    void tryMove(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2);

    void tryAttack(Point from, Point to, Field field, AbstractFigure f1, AbstractFigure f2);

    String toString();
}
