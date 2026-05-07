/**
 * Move action representing rightward movement (increasing x-coordinate).
 */
public class Right extends Move {

    /**
     * Creates a Right move action.
     *
     * @param initiator the player ID
     */
    public Right(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() + 1, currentPosition.y());
    }
}
