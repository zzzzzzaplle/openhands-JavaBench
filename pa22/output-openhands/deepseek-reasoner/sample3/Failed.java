/**
 * ActionResult indicating that an action failed, with a descriptive reason.
 */
public class Failed extends ActionResult {

    private final String reason;

    /**
     * Creates a Failed result for the given action.
     *
     * @param action the action that failed
     * @param reason description of why the action failed
     */
    public Failed(Action action, String reason) {
        super(action);
        this.reason = reason;
    }

    /**
     * Returns the reason for the failure.
     *
     * @return failure reason
     */
    public String getReason() {
        return reason;
    }
}
