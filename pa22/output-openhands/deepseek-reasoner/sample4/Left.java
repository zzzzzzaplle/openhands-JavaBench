/**
 * Represents a move leftward (decreasing x-coordinate).
 */
public class Left extends Move {

    /**
     * Create a left move action.
     *
     * @param initiator the player ID initiating the move
     */
    public Left(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x() - 1, currentPosition.y());
    }
}
