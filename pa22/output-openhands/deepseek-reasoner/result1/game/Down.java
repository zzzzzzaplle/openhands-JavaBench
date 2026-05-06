package assignment.game;

/**
 * A move action in the down direction (increasing y).
 */
public class Down extends Move {
    /**
     * Create a down move action.
     *
     * @param initiator the player ID who is moving down
     */
    public Down(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() + 1);
    }
}
