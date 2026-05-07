/**
 * Abstract base class for movement actions in specific directions.
 */
public abstract class Move extends Action {

    /**
     * Creates a Move action initiated by the specified player.
     *
     * @param initiator the player ID
     */
    protected Move(int initiator) {
        super(initiator);
    }

    /**
     * Computes the next position after moving from the current position in the
     * direction of this move.
     *
     * @param currentPosition the current position
     * @return the next position after moving
     */
    public abstract Position nextPosition(Position currentPosition);
}
