package assignment.game;

/**
 * Abstract base class for move actions (Up, Down, Left, Right).
 */
public abstract class Move extends Action {
    /**
     * Create a move action.
     *
     * @param initiator the player ID who is moving
     */
    protected Move(int initiator) {
        super(initiator);
    }

    /**
     * Calculate the next position from the given current position based on the direction.
     *
     * @param currentPosition the current position
     * @return the next position in the direction of this move
     */
    public abstract Position nextPosition(Position currentPosition);
}
