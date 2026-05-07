package assignment.game;

/**
 * Represents an upward movement action (decreasing y-coordinate).
 */
public class Up extends Move {

    /**
     * Creates an Up movement action.
     *
     * @param initiator the player ID moving up
     */
    public Up(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() - 1);
    }
}
