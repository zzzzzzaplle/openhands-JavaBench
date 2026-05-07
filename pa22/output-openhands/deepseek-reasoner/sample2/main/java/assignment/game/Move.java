package assignment.game;

/**
 * Abstract base class for movement actions in the four cardinal directions.
 */
public abstract class Move extends Action {

    /**
     * Creates a move action initiated by the specified player.
     *
     * @param initiator the player ID moving
     */
    protected Move(int initiator) {
        super(initiator);
    }

    /**
     * Computes the next position from the given current position based on the
     * direction of this movement.
     *
     * @param currentPosition the current position of the entity
     * @return the new position after applying this movement
     */
    public abstract Position nextPosition(Position currentPosition);
}
