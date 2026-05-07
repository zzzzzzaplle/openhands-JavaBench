/**
 * Represents a move rightward (increasing x-coordinate).
 */
public class Right extends Move {

    /**
     * Create a right move action.
     *
     * @param initiator the player ID initiating the move
     */
    public Right(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() + 1, currentPosition.y());
    }
}
