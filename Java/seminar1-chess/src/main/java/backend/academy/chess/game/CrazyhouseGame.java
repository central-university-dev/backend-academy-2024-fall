package backend.academy.chess.game;

import backend.academy.chess.figure.AbstractFigure;
import backend.academy.chess.figure.Color;
import backend.academy.chess.figure.impl.Pawns;
import backend.academy.chess.figure.impl.Queen;
import backend.academy.chess.game.field.Field;
import backend.academy.chess.game.field.Point;
import backend.academy.chess.player.Player;
import backend.academy.chess.player.User;

public class CrazyhouseGame extends Game {
    /************************
     * member
     ***********************/

    // just for better comprehensibility of the code: assign white and black player
    private Player blackPlayer;
    private Player whitePlayer;
    private Field field;

    private boolean check;

    // internal representation of the game state

    /************************
     * constructors
     ***********************/

    public CrazyhouseGame() {
        super();
        field = new Field(8, 8);
        setBoard(null);
        nextPlayer = whitePlayer;
    }

    /*******************************************
     * Game class functions already implemented
     ******************************************/

    @Override
    public boolean addPlayer(Player player) {
        if (!started) {
            players.add(player);

            // game starts with two players
            if (players.size() == 2) {
                started = true;
                this.whitePlayer = players.get(0);
                this.blackPlayer = players.get(1);
                nextPlayer = whitePlayer;
            }
            return true;
        }

        return false;
    }

    @Override
    public String nextPlayerString() {
        return isWhiteNext() ? "w" : "b";
    }

    @Override
    public boolean giveUp(Player player) {
        if (started && !finished) {
            if (this.whitePlayer == player) {
                whitePlayer.surrender();
                blackPlayer.setWinner();
            }
            if (this.blackPlayer == player) {
                blackPlayer.surrender();
                whitePlayer.setWinner();
            }
            surrendered = true;
            finish();

            return true;
        }

        return false;
    }

    public Player getPlayer(User u) {
        for (Player p : players) {
            if (p.getId() == u.getId()) {
                return p;
            }
        }
        return null;
    }
    /*
     * ****************************************** Helpful stuff
     */

    /**
     * @return True if it's white player's turn
     */
    public boolean isWhiteNext() {
        return nextPlayer == whitePlayer;
    }

    @Override
    public String getBoard() {
        return field.getBoard();
    }

    //test from this part
    @Override
    public void setBoard(String state) {
        if (state == null) {
            field.setDefaultBoard();
        } else {
            field.setCustomBoard(state);
        }
    }

    private Color getCurrentPlayerColor() {
        return nextPlayer == blackPlayer ? Color.BLACK : Color.WHITE;
    }

    private void changePlayer() {
        nextPlayer = getCurrentPlayerColor() == Color.BLACK ? whitePlayer : blackPlayer;
    }

    @Override
    public boolean tryMove(String moveString, Player player) {
        System.out.println("tryMove: " + moveString);
        try {
            if (isPlayersTurn(player)) {
                String[] sF = moveString.split("-");
                if (sF[0].length() == 1) {
                    place(sF[0], sF[1]);
                } else {
                    move(sF[0], sF[1]);
                }
                checkCheck(field, getCurrentPlayerColor().inverse());
                changePlayer();
                System.out.println("move -> true");
                field.prettyPrint();
                return true;
            }
            System.out.println("not your turn. current_Color:" + getCurrentPlayerColor());
            return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkCheck(Field field, Color color) {
        System.out.println("check check for: " + color);
        if (field.canAnyoneKillAKing(color)) {
            check = true;
            System.out.println("check for " + color);
        } else {
            check = false;
            System.out.println("no check for " + color);
        }
    }

    private Point getPoint(String address) {
        int let2 = (address.toCharArray()[0] - 'a');
        int num2 = 7 - (Integer.parseInt(String.valueOf(address.toCharArray()[1])) - 1);
        return new Point(num2, let2);
    }

    private void place(String figure, String to) {
        AbstractFigure f = AbstractFigure.create(figure);
        Point pointTo = getPoint(to);

        if (f instanceof Pawns) {
            if (pointTo.number == 0 || pointTo.number == 7) {
                throw new RuntimeException("you can't place pawn on 1 or 8 row");
            }
        }

        checkOwnFigure(f);

        if (field.getFigure(pointTo) != null) {
            throw new RuntimeException("Cant place figure in already taken field");
        }

        AbstractFigure toRemoveF = null;
        for (AbstractFigure poolAbstractFigure : field.getReserve()) {
            if (f.toString().equals(poolAbstractFigure.toString())) {
                toRemoveF = poolAbstractFigure;
                break;
            }
        }

        if (toRemoveF == null) {
            throw new RuntimeException("figure not in pool");
        } else {
            field.getReserve().remove(toRemoveF);
        }

        field.placeFigure(f, pointTo);
    }

    private void checkOwnFigure(AbstractFigure f) {
        if (nextPlayer == blackPlayer) {
            if (f.getColor() != Color.BLACK) {
                throw new RuntimeException("Not your figure");
            }
        } else {
            if (f.getColor() != Color.WHITE) {
                throw new RuntimeException("Not your figure");
            }
        }
    }

    private void move(String from, String to) {
        field.prettyPrint();

        Point pointFrom = getPoint(from);
        AbstractFigure f = field.getField()[pointFrom.number][pointFrom.letter];
        if (f == null) {
            throw new RuntimeException("figure not found");
        }

        Point pointTo = getPoint(to);
        AbstractFigure f2 = field.getField()[pointTo.number][pointTo.letter];

        if (nextPlayer == blackPlayer) {
            if (f.getColor() != Color.BLACK) {
                throw new RuntimeException("Not your figure");
            }
        } else if (nextPlayer == whitePlayer) {
            if (f.getColor() != Color.WHITE) {
                throw new RuntimeException("Not your figure");
            }
        }
        f.move(pointFrom, pointTo, field);

        Field temp = new Field(field);
        doFieldUpdate(temp, temp.getFigure(pointFrom), pointFrom, pointTo, temp.getFigure(pointTo));
        checkCheck(temp, getCurrentPlayerColor());
        if (check) {
            throw new RuntimeException("Your king under attack!");
        }

        doFieldUpdate(field, f, pointFrom, pointTo, f2);

    }

    private void doFieldUpdate(
        Field field,
        AbstractFigure f,
        Point pointFrom,
        Point pointTo,
        AbstractFigure f2
    ) {
        field.getField()[pointFrom.number][pointFrom.letter] = null;
        field.getField()[pointTo.number][pointTo.letter] = f;

        if (f2 != null) {
            f2.inverseColor();
            field.getReserve().add(f2);
        }

        if ((pointTo.number == 0 || pointTo.number == 7) && f instanceof Pawns) {
            f = new Queen(f.getColor());
            field.getField()[pointTo.number][pointTo.letter] = f;
        }
    }

}
