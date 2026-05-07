/**
 * Action representing a player's decision to exit the game.
 */
public class Exit extends Action {

    /**
     * Creates an Exit action.
     *
     * @param initiator the player ID, or -1 for system-triggered exit
     */
    public Exit(int initiator) {
        super(initiator);
    }
}
