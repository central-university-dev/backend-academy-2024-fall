package backend.academy.chess.game.field;

import backend.academy.chess.figure.AbstractFigure;
import backend.academy.chess.figure.impl.Bishop;
import backend.academy.chess.figure.impl.King;
import backend.academy.chess.figure.impl.Knight;
import backend.academy.chess.figure.impl.Pawns;
import backend.academy.chess.figure.impl.Queen;
import backend.academy.chess.figure.impl.Rook;
import backend.academy.chess.figure.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field {
    private AbstractFigure field[][];
    private List<AbstractFigure> reserve = new ArrayList<>();

    public Field(int x, int y) {
        field = new AbstractFigure[x][y];
    }

    public Field(Field fieldParam) {
        this.reserve.addAll(fieldParam.getReserve());

        field = new AbstractFigure[8][8];

        AbstractFigure[][] temp = fieldParam.field;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                if (fieldParam.field[i][j] != null) {
                    field[i][j] = AbstractFigure.create(fieldParam.field[i][j].toString());
                }
            }
        }
    }

    public List<AbstractFigure> getReserve() {
        return reserve;
    }

    public AbstractFigure[][] getField() {
        return field;
    }

    public AbstractFigure getFigure(Point point) {
        return field[point.number][point.letter];
    }

    public void placeFigure(AbstractFigure f, Point point) {
        field[point.number][point.letter] = f;
    }

    public void canGoThrough(Point from, Point to, int diffNum, int diffLet) {
        Point temp = new Point(from.number, from.letter);
        for (int i = 0; i < 8; i++) {
            temp = new Point(temp.number + diffNum, temp.letter + diffLet);
            if (this.getFigure(temp) != null) {
                throw new RuntimeException("Can't go through " + temp);
            }

            if (to.isSame(temp)) {
                break;
            }
        }
    }

    public boolean isKingAlive(Color color) {
        final AbstractFigure temp = new King(color);
        final String key = temp.toString();

        boolean found = false;
        for (AbstractFigure[] arr : field) {
            for (AbstractFigure f : arr) {
                if (f != null && f.toString().equals(key)) {
                    found = true;
                    break;
                }
            }
        }

        return found;

    }

    public void setCustomBoard(String state) {
        cleanField();
        String[] rows = state.split("/");

        for (int num = 0; num < 8; num++) {
            char[] figuresRow = rows[num].toCharArray();
            fillRow(num, figuresRow);
        }
        if (rows.length == 9) {
            addReserve(rows[8]);
        }

        System.out.println("custom field set: ");
        prettyPrint();
    }

    private void cleanField() {
        for (AbstractFigure[] abstractFigures : field) {
            Arrays.fill(abstractFigures, null);
        }
    }

    private void fillRow(int num, char[] figuresRow) {
        char[] expand = new char[8];
        int index = 0;
        for (int i = 0; i < figuresRow.length; i++) {
            if (Character.isDigit(figuresRow[i])) {
                int times = Integer.parseInt(String.valueOf(figuresRow[i]));
                while (times-- != 0) {
                    expand[index++] = '1';
                }
            } else {
                expand[index++] = figuresRow[i];
            }
        }

        for (int i = 0; i < expand.length; i++) {
            if (expand[i] != '1') {
                AbstractFigure f = AbstractFigure.create(expand[i]);
                placeFigure(f, new Point(num, i));
            }
        }

    }

    private void addReserve(String row) {
        for (char c : row.toCharArray()) {
            reserve.add(AbstractFigure.create(c));
        }
    }

    public void setDefaultBoard() {
        final AbstractFigure[][] currentField = getField();

        //init black
        currentField[0][0] = new Rook(Color.BLACK);
        currentField[0][1] = new Knight(Color.BLACK);
        currentField[0][2] = new Bishop(Color.BLACK);
        currentField[0][3] = new Queen(Color.BLACK);
        currentField[0][4] = new King(Color.BLACK);
        currentField[0][5] = new Bishop(Color.BLACK);
        currentField[0][6] = new Knight(Color.BLACK);
        currentField[0][7] = new Rook(Color.BLACK);

        currentField[1][0] = new Pawns(Color.BLACK);
        currentField[1][1] = new Pawns(Color.BLACK);
        currentField[1][2] = new Pawns(Color.BLACK);
        currentField[1][3] = new Pawns(Color.BLACK);
        currentField[1][4] = new Pawns(Color.BLACK);
        currentField[1][5] = new Pawns(Color.BLACK);
        currentField[1][6] = new Pawns(Color.BLACK);
        currentField[1][7] = new Pawns(Color.BLACK);

        //init white
        currentField[6][0] = new Pawns(Color.WHITE);
        currentField[6][1] = new Pawns(Color.WHITE);
        currentField[6][2] = new Pawns(Color.WHITE);
        currentField[6][3] = new Pawns(Color.WHITE);
        currentField[6][4] = new Pawns(Color.WHITE);
        currentField[6][5] = new Pawns(Color.WHITE);
        currentField[6][6] = new Pawns(Color.WHITE);
        currentField[6][7] = new Pawns(Color.WHITE);

        currentField[7][0] = new Rook(Color.WHITE);
        currentField[7][1] = new Knight(Color.WHITE);
        currentField[7][2] = new Bishop(Color.WHITE);
        currentField[7][3] = new Queen(Color.WHITE);
        currentField[7][4] = new King(Color.WHITE);
        currentField[7][5] = new Bishop(Color.WHITE);
        currentField[7][6] = new Knight(Color.WHITE);
        currentField[7][7] = new Rook(Color.WHITE);
    }

    public String getBoard() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < field.length; i++) {
            int count = 0;
            for (int j = 0; j < field[i].length; j++) {
                AbstractFigure f = field[i][j];
                if (f == null) {
                    count++;
                } else {
                    if (count != 0) {
                        sb.append(count);
                        count = 0;
                    }
                    sb.append(f);
                }

            }
            if (count != 0) {
                sb.append(count);
                count = 0;
            }
            sb.append("/");
        }

        reserve.forEach(el -> sb.append(el.toString()));

        return sb.toString();
    }

    public void prettyPrint() {
        System.out.println();

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                System.out.print(field[i][j] == null ? "1" : field[i][j]);
            }
            System.out.println();
        }

        System.out.println();
    }

    public Point findKing(Color color) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                AbstractFigure f = field[i][j];
                if (f instanceof King && f.getColor() == color) {
                    return new Point(i, j);
                }
            }
        }
        throw new RuntimeException("There is no king on the field");
    }

    public boolean canAnyoneKillAKing(Color attColor) {
        Point p = findKing(attColor);
        System.out.println("king is on..." + p);
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                AbstractFigure f = field[i][j];
                if (f != null && f.getColor() != attColor) {
                    try {
                        f.move(new Point(i, j), p, this);
                        System.out.println(
                            getFigure(new Point(i, j)) + " can kill " + getFigure(p).getColor() + " king");
                        return true;
                    } catch (RuntimeException ignored) {
                        //it's ok when someone can't kill a king
                    }
                }
            }
        }
        System.out.println("noone can kill " + getFigure(p).getColor() + " king");

        return false;
    }
}
