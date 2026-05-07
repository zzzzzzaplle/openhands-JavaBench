/**
 * Action representing a request to undo the last move that pushed a box.
 */
public class Undo extends Action {

    /**
     * Create a new undo action.
     *
     * @param initiator the player ID requesting the undo
     */
    public Undo(int initiator) {
        super(initiator);
    }
}
