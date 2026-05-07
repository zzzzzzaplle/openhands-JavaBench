/**
 * Abstract base class for movement actions. Each move action calculates the
 * next position based on the current position and direction.
 */
public abstract class Move extends Action {

    /**
     * Create a new move action.
     *
     * @param initiator the player ID attempting the move
     */
    protected Move(int initiator) {
        super(initiator);
    }

    /**
     * Calculate the next position after applying this move from the given position.
     *
     * @param currentPosition the current position
     * @return the new position after movement
     */
    public abstract Position nextPosition(Position currentPosition);
}
