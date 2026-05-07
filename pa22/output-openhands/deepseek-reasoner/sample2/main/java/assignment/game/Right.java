package assignment.game;

/**
 * Represents a rightward movement action (increasing x-coordinate).
 */
public class Right extends Move {

    /**
     * Creates a Right movement action.
     *
     * @param initiator the player ID moving right
     */
    public Right(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() + 1, currentPosition.y());
    }
}
