package assignment.game;

/**
 * Indicates that a game action failed to be processed, with a reason.
 */
public class Failed extends ActionResult {

    private final String reason;

    /**
     * Creates a Failed result for the given action with a reason.
     *
     * @param action the action that failed
     * @param reason a description of why the action failed
     */
    public Failed(Action action, String reason) {
        super(action);
        this.reason = reason;
    }

    /**
     * Returns the reason for the failure.
     *
     * @return the failure reason
     */
    public String getReason() {
        return reason;
    }
}
