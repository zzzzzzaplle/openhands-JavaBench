/**
 * Indicates that an action was executed successfully.
 */
public class Success extends ActionResult {

    /**
     * Create a success result for the given action.
     *
     * @param action the successfully executed action
     */
    public Success(Action action) {
        super(action);
    }
}
