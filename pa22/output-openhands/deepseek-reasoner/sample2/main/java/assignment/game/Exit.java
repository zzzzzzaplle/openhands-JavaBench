package assignment.game;

/**
 * Represents an exit/quit action that terminates the game.
 */
public class Exit extends Action {

    /**
     * Creates an exit action.
     *
     * @param initiator the player ID who initiated the exit, or -1 for system/command exit
     */
    public Exit(int initiator) {
        super(initiator);
    }
}
