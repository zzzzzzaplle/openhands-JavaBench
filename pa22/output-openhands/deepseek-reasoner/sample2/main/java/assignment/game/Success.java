package assignment.game;

/**
 * Indicates that a game action was successfully processed.
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
