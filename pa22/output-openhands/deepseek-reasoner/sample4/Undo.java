/**
 * Represents an action to undo the last checkpoint (box push).
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
