/**
 * Move action representing downward movement (increasing y-coordinate).
 */
public class Down extends Move {

    /**
     * Creates a Down move action.
     *
     * @param initiator the player ID
     */
    public Down(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() + 1);
    }
}
