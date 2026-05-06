package assignment.game;

/**
 * A move action in the right direction (increasing x).
 */
public class Right extends Move {
    /**
     * Create a right move action.
     *
     * @param initiator the player ID who is moving right
     */
    public Right(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() + 1, currentPosition.y());
    }
}
