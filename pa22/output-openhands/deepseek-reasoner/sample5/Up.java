/**
 * Move action representing an upward movement (decreasing y-coordinate).
 */
public class Up extends Move {

    /**
     * Create a new up move action.
     *
     * @param initiator the player ID attempting the move
     */
    public Up(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() - 1);
    }
}
