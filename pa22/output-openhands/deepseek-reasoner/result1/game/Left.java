package assignment.game;

/**
 * A move action in the left direction (decreasing x).
 */
public class Left extends Move {
    /**
     * Create a left move action.
     *
     * @param initiator the player ID who is moving left
     */
    public Left(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() - 1, currentPosition.y());
    }
}
