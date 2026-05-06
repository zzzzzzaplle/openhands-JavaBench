import java.util.Objects;

/**
 * A valid move where the player died (hit a mine).
 */
public class Dead extends Valid {

    /**
     * Creates a dead move result.
     *
     * @param origPosition The position before the move.
     * @param newPosition  The position where the player died.
     */
    public Dead(final Position origPosition, final Position newPosition) {
        super(Objects.requireNonNull(origPosition), Objects.requireNonNull(newPosition));
    }
}
