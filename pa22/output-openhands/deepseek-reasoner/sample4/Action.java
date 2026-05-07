/**
 * Abstract base class for all actions that can be performed in the game.
 */
public abstract class Action {

    private final int initiator;

    /**
     * Create an action initiated by the given player.
     *
     * @param initiator the player ID initiating this action, or -1 for system actions
     */
    protected Action(int initiator) {
        this.initiator = initiator;
    }

    /**
     * @return the player ID that initiated this action
     */
    public int getInitiator() {
        return initiator;
    }
}
