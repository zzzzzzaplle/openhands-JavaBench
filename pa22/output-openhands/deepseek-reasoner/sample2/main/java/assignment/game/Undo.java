package assignment.game;

/**
 * Represents an undo action that reverts the most recent checkpoint of game moves.
 */
public class Undo extends Action {

    /**
     * Creates an undo action initiated by the specified player.
     *
     * @param initiator the player ID requesting the undo
     */
    public Undo(int initiator) {
        super(initiator);
    }
}
