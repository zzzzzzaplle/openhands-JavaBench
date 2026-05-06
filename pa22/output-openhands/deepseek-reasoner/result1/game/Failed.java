package assignment.game;

/**
 * Indicates that an action failed with a reason.
 */
public class Failed extends ActionResult {
    private final String reason;

    /**
     * Create a failed result.
     *
     * @param action the action that failed
     * @param reason a description of why the action failed
     */
    public Failed(Action action, String reason) {
        super(action);
        this.reason = reason;
    }

    /**
     * @return the reason for the failure
     */
    public String getReason() {
        return reason;
    }
}
