/**
 * An invalid move result, returned when the player cannot move at all.
 */
public class Invalid extends MoveResult {

    /**
     * Creates an invalid move result at the given position.
     *
     * @param position The player's current position (no movement occurred).
     */
    public Invalid(final Position position) {
        super(position);
    }
}
