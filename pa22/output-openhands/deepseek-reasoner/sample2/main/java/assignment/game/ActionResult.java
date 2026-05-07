package assignment.game;

import java.util.Objects;

/**
 * Abstract base class representing the result of processing a game action.
 */
public abstract class ActionResult {

    protected final Action action;

    /**
     * Creates an ActionResult associated with the given action.
     *
     * @param action the action that was processed
     */
    protected ActionResult(Action action) {
        this.action = action;
    }

    /**
     * Returns the action associated with this result.
     *
     * @return the action
     */
    public Action getAction() {
        return action;
    }
}
