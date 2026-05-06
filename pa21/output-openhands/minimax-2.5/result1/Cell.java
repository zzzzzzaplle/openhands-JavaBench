package game;

/**
 * Abstract base class for cells on the game board.
 */
public abstract class Cell implements BoardElement {
    private Position position;

    public Cell(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}