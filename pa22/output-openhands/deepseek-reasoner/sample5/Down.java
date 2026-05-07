/**
 * Move action representing a downward movement (increasing y-coordinate).
 */
public class Down extends Move {

    /**
     * Create a new down move action.
     *
     * @param initiator the player ID attempting the move
     */
    public Down(int initiator) {
        super(initiator);
    }

    @Override
    public Position nextPosition(Position currentPosition) {
        return Position.of(currentPosition.x(), currentPosition.y() + 1);
    }
}
