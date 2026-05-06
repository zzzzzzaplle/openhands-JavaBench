package assignment.game;

/**
 * Indicates that an action was processed successfully.
 */
public class Success extends ActionResult {
    /**
     * Create a successful result.
     *
     * @param action the action that succeeded
     */
    public Success(Action action) {
        super(action);
    }
}
