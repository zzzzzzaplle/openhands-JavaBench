/**
 * Represents an action to exit the game.
 */
public class Exit extends Action {

    /**
     * Create an exit action.
     *
     * @param initiator the player ID or -1 for system-triggered exit
     */
    public Exit(int initiator) {
        super(initiator);
    }
}
