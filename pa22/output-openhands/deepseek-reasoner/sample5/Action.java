/**
 * Abstract base class for all actions that can be performed in the game.
 */
public abstract class Action {

    protected final int initiator;

    /**
     * Create a new action.
     *
     * @param initiator the player ID initiating the action, or -1 for system actions
     */
    protected Action(int initiator) {
        this.initiator = initiator;
    }

    /**
     * @return the player ID that initiated this action, or -1 for system actions
     */
    public int getInitiator() {
        return initiator;
    }
}
