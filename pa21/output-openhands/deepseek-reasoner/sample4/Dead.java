/**
 * Represents a valid move result where the player died from hitting a mine.
 */
public class Dead extends Valid {

    /**
     * Creates a dead move result.
     *
     * @param newPosition  The position after the move (reverted to original).
     * @param origPosition The original position before the move.
     */
    public Dead(final Position newPosition, final Position origPosition) {
        super(newPosition, origPosition);
    }
}
