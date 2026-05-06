package assignment.game;

/**
 * An action to undo the last move.
 */
public class Undo extends Action {
    /**
     * Create an undo action.
     *
     * @param initiator the player ID requesting the undo
     */
    public Undo(int initiator) {
        super(initiator);
    }
}
