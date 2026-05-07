/**
 * A valid move result where the player died from hitting a mine.
 * The player's position is reverted to the original position.
 */
public class Dead extends Valid {

    /**
     * Creates a dead move result. The player's position will be reverted to the original.
     *
     * @param origPosition The original position of the player before the move.
     * @param newPosition  The position where the mine was hit.
     */
    public Dead(final Position origPosition, final Position newPosition) {
        super(origPosition, newPosition);
    }
}
