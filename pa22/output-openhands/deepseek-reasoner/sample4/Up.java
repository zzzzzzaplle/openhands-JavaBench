/**
 * Represents a move upward (decreasing y-coordinate).
 */
public class Up extends Move {

    /**
     * Create an up move action.
     *
     * @param initiator the player ID initiating the move
     */
    public Up(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() - 1);
    }
}
