/**
 * Action result indicating that an action failed, with a description of why.
 */
public class Failed extends ActionResult {

    private final String reason;

    /**
     * Create a new failed result.
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
