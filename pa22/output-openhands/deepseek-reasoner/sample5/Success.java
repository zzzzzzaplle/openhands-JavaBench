/**
 * Action result indicating that an action was successfully processed.
 */
public class Success extends ActionResult {

    /**
     * Create a new success result.
     *
     * @param action the action that succeeded
     */
    public Success(Action action) {
        super(action);
    }
}
