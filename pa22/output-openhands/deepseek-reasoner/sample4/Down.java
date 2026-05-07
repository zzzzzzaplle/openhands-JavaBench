/**
 * Represents a move downward (increasing y-coordinate).
 */
public class Down extends Move {

    /**
     * Create a down move action.
     *
     * @param initiator the player ID initiating the move
     */
    public Down(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() + 1);
    }
}
