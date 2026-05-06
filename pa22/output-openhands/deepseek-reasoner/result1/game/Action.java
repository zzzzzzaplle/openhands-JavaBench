package assignment.game;

/**
 * Abstract base class representing a user action in the game.
 */
public abstract class Action {
    private final int initiator;

    /**
     * Create an action with the given initiator.
     *
     * @param initiator the player ID who initiated the action, or -1 for system actions
     */
    protected Action(int initiator) {
        this.initiator = initiator;
    }

    /**
     * @return the player ID who initiated this action, or -1 for system actions
     */
    public int getInitiator() {
        return initiator;
    }
}
