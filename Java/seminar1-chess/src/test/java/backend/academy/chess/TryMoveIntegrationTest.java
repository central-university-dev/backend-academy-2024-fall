package backend.academy.chess;

import backend.academy.chess.controll.GameController;
import backend.academy.chess.game.Game;
import backend.academy.chess.player.Player;
import backend.academy.chess.player.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TryMoveIntegrationTest {

    User user1 = new User(1, "alice");
    User user2 = new User(-1, "haskellbot");

    Player whitePlayer = null;
    Player blackPlayer = null;
    Game game = null;
    GameController controller;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new GameController();

        game = controller.startGameWithBot(user1, GameController.CRAZYHOUSE);

        whitePlayer = game.getPlayer(user1);

    }

    public void startGame() {
        blackPlayer = game.getPlayer(user2);
    }

    public void startGame(String initialBoard, boolean whiteNext) {
        startGame();

        game.setBoard(initialBoard);
        game.setNextPlayer(whiteNext ? whitePlayer : blackPlayer);
    }

    public void assertMove(String move, boolean white, boolean expectedResult) {
        if (white) {
            assertEquals(expectedResult, game.tryMove(move, whitePlayer));
        } else {
            assertEquals(expectedResult, game.tryMove(move, blackPlayer));
        }

    }

    public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
        String board = game.getBoard().replaceAll("e", "");

        assertEquals(expectedBoard, board);
        assertEquals(finished, game.isFinished());

        if (!game.isFinished()) {
            assertEquals(whiteNext, game.getNextPlayer() == whitePlayer);
        } else {
            assertEquals(whiteWon, whitePlayer.isWinner());
            assertEquals(!whiteWon, blackPlayer.isWinner());
        }
    }

    // White Figures

    // Pawns

    @Test
    public void tryMove_WhitePawnMove2Cells_Success() {
        startGame("7K/k7/8/8/8/8/4P3/8/", true);
        assertMove("e2-e4", true, true);
        assertGameState("7K/k7/8/8/4P3/8/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove1Cell_Success() {
        startGame("7K/k7/8/8/8/8/4P3/8/", true);
        assertMove("e2-e3", true, true);
        assertGameState("7K/k7/8/8/8/4P3/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove1CellNormalRow_Success() {
        startGame("7K/k7/8/8/8/4P3/8/8/", true);
        assertMove("e3-e4", true, true);
        assertGameState("7K/k7/8/8/4P3/8/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove1CellNormalRowTaken_Failure() {
        startGame("7K/k7/8/8/4p3/4P3/8/8/", true);
        assertMove("e3-e4", true, false);
        assertGameState("7K/k7/8/8/4p3/4P3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove1Cellto8Row_Failure() {
        startGame("7K/4P3/8/8/8/k7/8/8/", true);
        assertMove("e7-e8", true, true);
        assertGameState("4Q2K/8/8/8/8/k7/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove2CellNormalRow_Failure() {
        startGame("7K/k7/8/8/8/4P3/8/8/", true);
        assertMove("e3-e5", true, false);
        assertGameState("7K/k7/8/8/8/4P3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnMoveDiag1Cell_Failure() {
        startGame("7K/k7/8/8/8/4P3/8/8/", true);
        assertMove("e3-f4", true, false);
        assertGameState("7K/k7/8/8/8/4P3/8/8/", true, false, false);
    }

    @Test
    public void tryAttack_WhitePawnEatDiag1Cell_Success() {
        startGame("7K/k7/8/8/5p2/4P3/8/8/", true);
        assertMove("e3-f4", true, true);
        assertGameState("7K/k7/8/8/5P2/8/8/8/P", false, false, false);
    }

    @Test
    public void tryAttack_BlackPawnEatDiag1Cell_Success() {
        startGame("7K/k7/8/8/5p2/4P3/8/8/", false);
        assertMove("f4-e3", false, true);
        assertGameState("7K/k7/8/8/8/4p3/8/8/p", true, false, false);
    }

    @Test
    public void tryAttack_WhitePawnEatDiag2Cell_Failure() {
        startGame("7K/k7/8/8/5p2/4P3/8/8/", true);
        assertMove("e3-g5", true, false);
        assertGameState("7K/k7/8/8/5p2/4P3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnBecomesQueen_Failure() {
        startGame("7K/4P3/8/8/8/k7/8/8/", true);
        assertMove("e6-e7", true, false);
        assertGameState("7K/4P3/8/8/8/k7/8/8/", true, false, false);
    }

    @Test
    public void tryAttack_WhitePawnBecomesQueen_Success() {
        startGame("5p2/4P3/8/8/7K/k7/8/8/", true);
        assertMove("e7-f8", true, true);
        assertGameState("5Q2/8/8/8/7K/k7/8/8/P", false, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove1CellBack_Failure() {
        startGame("7K/7k/8/8/8/4P3/8/8/", true);
        assertMove("e3-e2", true, false);
        assertGameState("7K/7k/8/8/8/4P3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnFigureOnTheWay_Failure() {
        startGame("7K/k7/8/8/4P3/8/4P3/8/", true);
        assertMove("e2-e4", true, false);
        assertGameState("7K/k7/8/8/4P3/8/4P3/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnNoLetChange_Failure() {
        startGame("7K/k7/8/8/8/8/4P3/8/", true);
        assertMove("e2-e2", true, false);
        assertGameState("7K/k7/8/8/8/8/4P3/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnMove3Cells_Failure() {
        startGame("7K/k7/8/8/8/8/4P3/8/", true);
        assertMove("e2-e5", true, false);
        assertGameState("7K/k7/8/8/8/8/4P3/8/", true, false, false);
    }

    @Test
    public void tryMove_WhitePawnWhenBlack_Failure() {
        startGame("7K/k7/8/8/8/4p3/4P3/8/", false);
        assertMove("e2-e3", false, false);
        assertGameState("7K/k7/8/8/8/4p3/4P3/8/", false, false, false);
    }

    @Test
    public void tryMove_BlackPawnWhenWhite_Failure() {
        startGame("7K/k7/8/8/4P3/8/4p3/8/", true);
        assertMove("e2-e3", true, false);
        assertGameState("7K/k7/8/8/4P3/8/4p3/8/", true, false, false);
    }

    // RESERVE
    @Test
    public void tryPlace_WhitePawnFromReserve_Success() {
        startGame("7K/k7/8/8/8/4P3/8/8/P", true);
        assertMove("P-e2", true, true);
        assertGameState("7K/k7/8/8/8/4P3/4P3/8/", false, false, false);
    }

    @Test
    public void tryPlace_WhitePawnFromReserve_Failure() {
        startGame("7K/k7/8/8/4p3/4P3/8/8/", true);
        assertMove("e3-e4", true, false);
        assertGameState("7K/k7/8/8/4p3/4P3/8/8/", true, false, false);
    }

    @Test
    public void tryPlace_WhitePawnFromReserveWhenBlack_Failure() {
        startGame("7K/k7/8/8/8/8/8/8/Pp", false);
        assertMove("P-e4", false, false);
        assertGameState("7K/k7/8/8/8/8/8/8/Pp", false, false, false);
    }

    @Test
    public void tryPlace_BlackPawnFromReserveWhenWhite_Failure() {
        startGame("7K/k7/8/8/8/8/8/8/Pp", true);
        assertMove("p-e4", true, false);
        assertGameState("7K/k7/8/8/8/8/8/8/Pp", true, false, false);
    }

    @Test
    public void tryPlace_WhitePawnFromReserveAlreadyTaken_Failure() {
        startGame("7K/k7/8/8/4P3/8/8/8/Pp", true);
        assertMove("P-e4", true, false);
        assertGameState("7K/k7/8/8/4P3/8/8/8/Pp", true, false, false);
    }

    @Test
    public void tryPlace_WhitePawnFromReserveOn1Row_Failure() {
        startGame("7K/k7/8/8/4P3/8/8/8/Pp", true);
        assertMove("P-e1", true, false);
        assertGameState("7K/k7/8/8/4P3/8/8/8/Pp", true, false, false);
    }

    @Test
    public void tryAttack_WhitePawnOwnFigure_Failure() {
        startGame("7K/k7/8/8/4P3/4P3/8/8/", true);
        assertMove("e3-e4", true, false);
        assertGameState("7K/k7/8/8/4P3/4P3/8/8/", true, false, false);
    }

    // Rook

    @Test
    public void tryMove_WhiteRookMove1CellUp_Success() {
        startGame("7K/k7/8/8/8/8/4R3/8/", true);
        assertMove("e2-e3", true, true);
        assertGameState("7K/k7/8/8/8/4R3/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhiteRookMove3CellsUp_Success() {
        startGame("7K/k7/8/8/8/8/4R3/8/", true);
        assertMove("e2-e5", true, true);
        assertGameState("7K/k7/8/4R3/8/8/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhiteRookMove8Row_Success() {
        startGame("7K/k7/8/8/8/8/4R3/8/", true);
        assertMove("e2-e8", true, true);
        assertGameState("4R2K/k7/8/8/8/8/8/8/", false, false, false);
    }

    @Test
    public void tryAttack_WhiteRookMove3CellsUp_Success() {
        startGame("7K/k7/8/4p3/8/8/4R3/8/", true);
        assertMove("e2-e5", true, true);
        assertGameState("7K/k7/8/4R3/8/8/8/8/P", false, false, false);
    }

    @Test
    public void tryMove_RookMoveLeftWhite() {
        startGame("8/8/8/3R4/8/8/8/K1k5/", true);
        assertMove("d5-c5", true, true);
        assertGameState("8/8/8/2R5/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void tryMove_WhiteRookMoveDiag1Cell_Failure() {
        startGame("7K/k7/8/8/8/8/4R3/8/", true);
        assertMove("e2-f3", true, false);
        assertGameState("7K/k7/8/8/8/8/4R3/8/", true, false, false);
    }

    @Test
    public void tryAttack_WhiteRookMove1CellUp_Success() {
        startGame("7K/k7/8/8/8/8/4R3/8/", true);
        assertMove("e2-e3", true, true);
        assertGameState("7K/k7/8/8/8/4R3/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhiteRookMove1CellRight_Success() {
        startGame("7K/k7/8/8/8/8/4R3/8/", true);
        assertMove("e2-f2", true, true);
        assertGameState("7K/k7/8/8/8/8/5R2/8/", false, false, false);
    }

    @Test
    public void RookMoveRightWhite() {
        startGame("8/8/8/3R4/8/8/8/K1k5/", true);
        assertMove("d5-e5", true, true);
        assertGameState("8/8/8/4R3/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void RookMoveLeftWhite() {
        startGame("8/8/8/3R4/8/8/8/K1k5/", true);
        assertMove("d5-c5", true, true);
        assertGameState("8/8/8/2R5/8/8/8/K1k5/", false, false, false);
    }

    // King

    @Test
    public void tryMove_WhiteKingMove1CellUp_Success() {
        startGame("8/k7/8/8/8/8/4K3/8/", true);
        assertMove("e2-e3", true, true);
        assertGameState("8/k7/8/8/8/4K3/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhiteKingMove1CellLeft_Success() {
        startGame("8/k7/8/8/8/4K3/8/8/", true);
        assertMove("e3-d3", true, true);
        assertGameState("8/k7/8/8/8/3K4/8/8/", false, false, false);
    }

    @Test
    public void tryMove_WhiteKingMove1CellRowTaken_Failure() {
        startGame("8/k7/8/8/4p3/4K3/8/8/", true);
        assertMove("e3-e4", true, true);
        assertGameState("8/k7/8/8/4K3/8/8/8/P", false, false, false);
    }

    @Test
    public void tryMove_WhiteKingMove2CellRight_Failure() {
        startGame("8/k7/8/8/8/4K3/8/8/", true);
        assertMove("e3-g3", true, false);
        assertGameState("8/k7/8/8/8/4K3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_WhiteKingMoveDiag2Cell_Failure() {
        startGame("8/k7/8/8/8/4K3/8/8/", true);
        assertMove("e3-g5", true, false);
        assertGameState("8/k7/8/8/8/4K3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_WhiteKingMoveDiag1Cell_Success() {
        startGame("8/k7/8/8/8/4K3/8/8/", true);
        assertMove("e3-f4", true, true);
        assertGameState("8/k7/8/8/5K2/8/8/8/", false, false, false);
    }

    @Test
    public void tryAttack_WhiteKingEatDiag1Cell_Success() {
        startGame("8/k7/8/8/5p2/4K3/8/8/", true);
        assertMove("e3-f4", true, true);
        assertGameState("8/k7/8/8/5K2/8/8/8/P", false, false, false);
    }

    @Test
    public void tryAttack_WhiteKingEat1CellUp_Success() {
        startGame("8/k7/8/8/4p3/4K3/8/8/", true);
        assertMove("e3-e4", true, true);
        assertGameState("8/k7/8/8/4K3/8/8/8/P", false, false, false);
    }

    @Test
    public void tryAttack_WhiteKingEatDiag2Cell_Failure() {
        startGame("8/k7/8/8/5p2/4K3/8/8/", true);
        assertMove("e3-g5", true, false);
        assertGameState("8/k7/8/8/5p2/4K3/8/8/", true, false, false);
    }

    @Test
    public void tryMove_Checkmate_Success() {
        startGame("k7/1K6/8/8/8/8/8/8/", true);
        assertMove(null, true, false);
        assertGameState("k7/1K6/8/8/8/8/8/8/", true, false, true);
    }

    @Test
    public void tryMove_NoBlackKing_Failure() {
        startGame("k7/K7/8/8/8/8/8/8/", false);
        assertMove("e7-e8", true, false);
        assertGameState("k7/K7/8/8/8/8/8/8/", false, false, false);
    }

    // Bishop

    @Test
    public void BishopMoveUpRightWhite() {
        startGame("8/8/8/3B4/8/8/8/K1k5/", true);
        assertMove("d5-e4", true, true);
        assertGameState("8/8/8/8/4B3/8/8/K1k5/", false, false, false);
    }

    @Test
    public void BishopMoveDownRightWhite() {
        startGame("8/8/8/3B4/8/8/8/K1k5/", true);
        assertMove("d5-e6", true, true);
        assertGameState("8/8/4B3/8/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void BishopMoveDownLeftWhite() {
        startGame("8/8/8/3B4/8/8/8/K1k5/", true);
        assertMove("d5-c6", true, true);
        assertGameState("8/8/2B5/8/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void BishopMoveUpLeftWhite() {
        startGame("8/8/8/3B4/8/8/8/K1k5/", true);
        assertMove("d5-c6", true, true);
        assertGameState("8/8/2B5/8/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void BishopMoveUpLeftBlack() {
        startGame("8/8/8/3b4/8/8/8/K1k5/", false);
        assertMove("d5-c6", false, true);
        assertGameState("8/8/2b5/8/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void BishopMoveStayWhite() {
        startGame("8/8/8/3B4/8/8/8/K1k5/", true);
        assertMove("d5-d5", true, false);
        assertGameState("8/8/8/3B4/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void BishopMoveToSamePieceWhite() {
        startGame("8/8/2R5/3B4/8/8/8/K1k5/", true);
        assertMove("d5-c6", true, false);
        assertGameState("8/8/2R5/3B4/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void BishopMovePastPieceWhite() {
        startGame("8/8/8/3B4/4R3/8/8/K1k5/", true);
        assertMove("d5-f3", true, false);
        assertGameState("8/8/8/3B4/4R3/8/8/K1k5/", true, false, false);
    }

    @Test
    public void tryAttack_WhiteBishopMove1CellUp_Success() {
        startGame("8/8/8/8/8/5p2/4B3/K1k5/", true);
        assertMove("e2-f3", true, true);
        assertGameState("8/8/8/8/8/5B2/8/K1k5/P", false, false, false);
    }

    // Knight
    @Test
    public void KnightMoveWhite_Success() {
        startGame("8/8/8/3N4/8/8/8/K1k5/", true);
        assertMove("d5-e7", true, true);
        assertGameState("8/4N3/8/8/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void KnightMoveBlack_Success() {
        startGame("8/8/8/3n4/8/8/8/K1k5/", false);
        assertMove("d5-e7", false, true);
        assertGameState("8/4n3/8/8/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void KnightAttackWhite_Success() {
        startGame("8/4r3/8/3N4/8/8/8/K1k5/", true);
        assertMove("d5-e7", true, true);
        assertGameState("8/4N3/8/8/8/8/8/K1k5/R", false, false, false);
    }

    @Test
    public void KnightMoveSamePieceWhite_Failure() {
        startGame("8/4R3/8/3N4/8/8/8/K1k5/", true);
        assertMove("d5-e7", true, false);
        assertGameState("8/4R3/8/3N4/8/8/8/K1k5/", true, false, false);
    }

    // Queen
    @Test
    public void QueenMoveUpWhite() {
        startGame("8/8/8/3Q4/8/8/8/K1k5", true);
        assertMove("d5-d6", true, true);
        assertGameState("8/8/3Q4/8/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void QueenMoveUpBlack() {
        startGame("8/8/8/3q4/8/8/8/K1k5", false);
        assertMove("d5-d6", false, true);
        assertGameState("8/8/3q4/8/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void QueenMoveUpRightWhite() {
        startGame("8/8/8/3Q4/8/8/8/K1k5", true);
        assertMove("d5-e6", true, true);
        assertGameState("8/8/4Q3/8/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void QueenMoveRightWhite() {
        startGame("8/8/8/3Q4/8/8/8/K1k5", true);
        assertMove("d5-e5", true, true);
        assertGameState("8/8/8/4Q3/8/8/8/K1k5/", false, false, false);
    }

    @Test
    public void QueenMoveDownRightWhite() {
        startGame("8/8/8/3Q4/8/8/8/K1k5", true);
        assertMove("d5-e4", true, true);
        assertGameState("8/8/8/8/4Q3/8/8/K1k5/", false, false, false);
    }

    @Test
    public void QueenMoveDownWhite() {
        startGame("8/8/8/3Q4/8/8/8/K1k5/", true);
        assertMove("d5-d4", true, true);
        assertGameState("8/8/8/8/3Q4/8/8/K1k5/", false, false, false);
    }

    @Test
    public void QueenMoveDownWhite_Failure() {
        startGame("8/8/8/3Q4/8/8/8/K1k5/", true);
        assertMove("d5-e8", true, false);
        assertGameState("8/8/8/3Q4/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void QueenMoveThroughWhite() {
        startGame("8/8/3R4/3Q4/8/8/8/K1k5", true);
        assertMove("d5-d8", true, false);
        assertGameState("8/8/3R4/3Q4/8/8/8/K1k5/", true, false, false);
    }

    @Test
    public void topAttack() {
        startGame("8/8/8/8/4k3/8/8/K6Q/", true);
        assertMove("h1-d1", true, true);
        assertGameState("8/8/8/8/4k3/8/8/K2Q4/", false, false, false);
    }

    @Test
    public void leftAttack() {
        startGame("Q6Q/8/8/1pppppp1/4k3/8/8/K7/", true);
        assertMove("a8-a4", true, true);
        assertGameState("7Q/8/8/1pppppp1/Q3k3/8/8/K7/", false, false, false);
    }

    @Test
    public void rightAttack() {
        startGame("Q6Q/8/8/1pppppp1/4k3/8/8/K7/", true);
        assertMove("h8-h4", true, true);
        assertGameState("Q7/8/8/1pppppp1/4k2Q/8/8/K7/", false, false, false);
    }

    @Test
    public void upLeftAtt() {
        startGame("Q6Q/8/8/1pppppp1/4k3/8/8/K7/", true);
        assertMove("h8-h1", true, true);
        assertGameState("Q7/8/8/1pppppp1/4k3/8/8/K6Q/", false, false, false);
    }

}
