package assignment.game;

/**
 * Abstract base class for all game actions.
 * Each action is initiated by a player identified by their initiator ID.
 */
public abstract class Action {

    /** The ID of the player who initiated this action, or -1 for system actions. */
    protected final int initiator;

    /**
     * Creates an action with the specified initiator.
     *
     * @param initiator the player ID who initiated the action, or -1 for system actions
     */
    protected Action(int initiator) {
        this.initiator = initiator;
    }

    /**
     * Returns the ID of the player who initiated this action.
     *
     * @return the initiator player ID
     */
    public int getInitiator() {
        return initiator;
    }
}
