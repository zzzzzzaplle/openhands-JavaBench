import java.util.Objects;

/**
 * A valid move result with an origin position.
 */
public abstract class Valid extends MoveResult {

    private final Position origPosition;

    /**
     * Creates a valid move result.
     *
     * @param origPosition The position before the move.
     * @param newPosition  The position after the move.
     */
    protected Valid(final Position origPosition, final Position newPosition) {
        super(Objects.requireNonNull(newPosition));
        this.origPosition = Objects.requireNonNull(origPosition);
    }

    /**
     * Returns the position before the move.
     *
     * @return The original position.
     */
    public Position getOrigPosition() {
        return origPosition;
    }
}
