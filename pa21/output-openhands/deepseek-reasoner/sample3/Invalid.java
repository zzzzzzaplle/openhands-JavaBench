/**
 * A move result indicating the move was invalid (player could not move at all).
 */
public class Invalid extends MoveResult {

    /**
     * Creates an invalid move result.
     *
     * @param position The player's current position (unchanged).
     */
    public Invalid(final Position position) {
        super(position);
    }
}
