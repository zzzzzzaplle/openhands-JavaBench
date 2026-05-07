/**
 * ActionResult indicating that an action was processed successfully.
 */
public class Success extends ActionResult {

    /**
     * Creates a Success result for the given action.
     *
     * @param action the successfully processed action
     */
    public Success(Action action) {
        super(action);
    }
}
