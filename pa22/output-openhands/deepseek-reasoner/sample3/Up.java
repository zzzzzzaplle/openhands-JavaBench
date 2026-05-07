/**
 * Move action representing upward movement (decreasing y-coordinate).
 */
public class Up extends Move {

    /**
     * Creates an Up move action.
     *
     * @param initiator the player ID
     */
    public Up(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() - 1);
    }
}
