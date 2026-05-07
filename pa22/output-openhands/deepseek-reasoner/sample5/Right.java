/**
 * Move action representing a rightward movement (increasing x-coordinate).
 */
public class Right extends Move {

    /**
     * Create a new right move action.
     *
     * @param initiator the player ID attempting the move
     */
    public Right(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() + 1, currentPosition.y());
    }
}
