import java.util.Objects;

/**
 * Abstract base class for valid move results (alive or dead).
 */
public abstract class Valid extends MoveResult {

    private final Position origPosition;

    /**
     * Creates a valid move result.
     *
     * @param origPosition The original position before the move.
     * @param newPosition  The position after the move.
     */
    protected Valid(final Position origPosition, final Position newPosition) {
        super(newPosition);
        this.origPosition = Objects.requireNonNull(origPosition);
    }

    /**
     * Returns the original position before the move.
     *
     * @return The original position.
     */
    public Position getOrigPosition() {
        return origPosition;
    }
}
