import java.util.Objects;

/**
 * Abstract base class for all cells on the game board.
 */
public abstract class Cell implements BoardElement {

    private final Position position;

    /**
     * Creates a cell at the specified position.
     *
     * @param position The position of this cell on the game board.
     */
    public Cell(final Position position) {
        this.position = Objects.requireNonNull(position);
    }

    /**
     * Returns the position of this cell.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }
}
