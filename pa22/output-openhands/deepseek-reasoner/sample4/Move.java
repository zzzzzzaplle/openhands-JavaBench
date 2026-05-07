/**
 * Abstract base class for movement actions in a specific direction.
 */
public abstract class Move extends Action {

    /**
     * Create a move action initiated by the given player.
     *
     * @param initiator the player ID initiating the move
     */
    protected Move(int initiator) {
        super(initiator);
    }

    /**
     * Calculate the resulting position after moving one step from the current position.
     *
     * @param currentPosition the current position
     * @return the next position in the direction of this move
     */
    public abstract Position nextPosition(Position currentPosition);
}
