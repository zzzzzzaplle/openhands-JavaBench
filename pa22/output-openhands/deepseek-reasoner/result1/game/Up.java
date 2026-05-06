package assignment.game;

/**
 * A move action in the up direction (decreasing y).
 */
public class Up extends Move {
    /**
     * Create an up move action.
     *
     * @param initiator the player ID who is moving up
     */
    public Up(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() - 1);
    }
}
