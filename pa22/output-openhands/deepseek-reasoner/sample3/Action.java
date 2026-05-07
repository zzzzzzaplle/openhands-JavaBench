/**
 * Abstract base class for all game actions that can be performed by a player.
 */
public abstract class Action {

    private final int initiator;

    /**
     * Creates an action initiated by the specified player.
     *
     * @param initiator the player ID initiating the action, or -1 for system actions
     */
    protected Action(int initiator) {
        this.initiator = initiator;
    }

    /**
     * Returns the player ID that initiated this action.
     *
     * @return initiator player ID
     */
    public int getInitiator() {
        return initiator;
    }
}
