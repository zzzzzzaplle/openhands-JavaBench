/**
 * Action representing a request to exit the game.
 */
public class Exit extends Action {

    /**
     * Create a new exit action.
     *
     * @param initiator the player ID, or -1 for system/command-triggered exit
     */
    public Exit(int initiator) {
        super(initiator);
    }
}
