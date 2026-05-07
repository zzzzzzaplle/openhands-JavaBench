package assignment.game;

/**
 * Represents a downward movement action (increasing y-coordinate).
 */
public class Down extends Move {

    /**
     * Creates a Down movement action.
     *
     * @param initiator the player ID moving down
     */
    public Down(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() + 1);
    }
}
