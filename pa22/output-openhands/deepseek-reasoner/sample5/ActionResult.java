/**
 * Abstract base class for the result of processing an action.
 */
public abstract class ActionResult {

    protected final Action action;

    /**
     * Create a new action result.
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
