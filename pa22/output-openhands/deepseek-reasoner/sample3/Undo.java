/**
 * Action representing a player's request to undo the last checkpoint.
 */
public class Undo extends Action {

    /**
     * Creates an Undo action.
     *
     * @param initiator the player requesting the undo
     */
    public Undo(int initiator) {
        super(initiator);
    }
}
