package assignment.game;

/**
 * Represents a leftward movement action (decreasing x-coordinate).
 */
public class Left extends Move {

    /**
     * Creates a Left movement action.
     *
     * @param initiator the player ID moving left
     */
    public Left(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() - 1, currentPosition.y());
    }
}
