import java.util.Objects;

/**
 * Abstract base class representing the result of a move.
 */
public abstract class MoveResult {

    private final Position newPosition;

    /**
     * Creates a move result with the specified new position.
     *
     * @param newPosition The position after the move.
     */
    public MoveResult(final Position newPosition) {
        this.newPosition = Objects.requireNonNull(newPosition);
    }

    /**
     * Returns the position after the move (or original position if invalid/dead).
     *
     * @return The new position.
     */
    public Position getNewPosition() {
        return newPosition;
    }
}
