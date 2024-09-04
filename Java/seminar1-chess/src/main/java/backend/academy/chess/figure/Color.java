package backend.academy.chess.figure;

public enum Color {
    WHITE, BLACK;

    public Color inverse() {
        return this == WHITE ? BLACK : WHITE;
    }
}
