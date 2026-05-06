package game;

/**
 * Abstract base class for action results.
 */
public abstract class ActionResult {
    protected Action action;

    protected ActionResult(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}

/**
 * Successful action result.
 */
class Success extends ActionResult {
    public Success(Action action) {
        super(action);
    }
}

/**
 * Failed action result with reason.
 */
class Failed extends ActionResult {
    private String reason;

    public Failed(Action action, String reason) {
        super(action);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}