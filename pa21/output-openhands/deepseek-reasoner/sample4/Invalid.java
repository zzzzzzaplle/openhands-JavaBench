/**
 * Represents an invalid move result where the player could not move at all.
 */
public class Invalid extends MoveResult {

    /**
     * Creates an invalid move result.
     *
     * @param position The position where the player didn't move (same as starting position).
     */
    public Invalid(final Position position) {
        super(position);
    }
}
