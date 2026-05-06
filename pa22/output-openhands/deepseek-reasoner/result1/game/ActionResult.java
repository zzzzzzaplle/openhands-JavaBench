package assignment.game;

/**
 * Abstract base class representing the result of processing an action.
 */
public abstract class ActionResult {
    private final Action action;

    /**
     * Create an action result.
     *
     * @param action the action that was processed
     */
    protected ActionResult(Action action) {
        this.action = action;
    }

    /**
     * @return the action that produced this result
     */
    public Action getAction() {
        return action;
    }
}
