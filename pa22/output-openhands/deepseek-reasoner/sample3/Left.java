/**
 * Move action representing leftward movement (decreasing x-coordinate).
 */
public class Left extends Move {

    /**
     * Creates a Left move action.
     *
     * @param initiator the player ID
     */
    public Left(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() - 1, currentPosition.y());
    }
}
