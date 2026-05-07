/**
 * Move action representing a leftward movement (decreasing x-coordinate).
 */
public class Left extends Move {

    /**
     * Create a new left move action.
     *
     * @param initiator the player ID attempting the move
     */
    public Left(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() - 1, currentPosition.y());
    }
}
