/**
 * A valid move result where the player died (hit a mine).
 */
public class Dead extends Valid {

    /**
     * Creates a dead move result. The player reverted to the original position.
     *
     * @param origPosition The original position before the move (also the position after revert).
     * @param newPosition  The position where the mine was hit (same as origPosition after revert).
     */
    public Dead(final Position origPosition, final Position newPosition) {
        super(origPosition, newPosition);
    }
}
