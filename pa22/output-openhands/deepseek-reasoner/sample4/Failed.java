/**
 * Indicates that an action failed to execute.
 */
public class Failed extends ActionResult {

    private final String reason;

    /**
     * Create a failed result for the given action.
     *
     * @param action the action that failed
     * @param reason a description of why the action failed
     */
    public Failed(Action action, String reason) {
        super(action);
        this.reason = reason;
    }

    /**
     * @return the reason for failure
     */
    public String getReason() {
        return reason;
    }
}
