import java.util.Objects;

/**
 * Abstract base class for the result of a move.
 */
public abstract class MoveResult {

    private final Position newPosition;

    /**
     * Creates a move result with the given new position.
     *
     * @param newPosition The position after the move.
     */
    protected MoveResult(final Position newPosition) {
        this.newPosition = Objects.requireNonNull(newPosition);
    }

    /**
     * Returns the position after the move.
     *
     * @return The new position.
     */
    public Position getNewPosition() {
        return newPosition;
    }
}
