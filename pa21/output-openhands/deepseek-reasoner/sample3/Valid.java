import java.util.Objects;

/**
 * Abstract base class for valid move results (the player moved at least one step).
 */
public abstract class Valid extends MoveResult {

    private final Position origPosition;

    /**
     * Creates a valid move result.
     *
     * @param origPosition The original position of the player before the move.
     * @param newPosition  The position the player ended up at after the move.
     */
    protected Valid(final Position origPosition, final Position newPosition) {
        super(newPosition);
        this.origPosition = Objects.requireNonNull(origPosition);
    }

    /**
     * Returns the original position of the player before the move.
     *
     * @return The original position.
     */
    public Position getOrigPosition() {
        return origPosition;
    }
}
