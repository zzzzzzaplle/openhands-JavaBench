/**
 * Abstract base implementation of SokobanGame that handles core game action processing
 * and manages the exit flag.
 */
public abstract class AbstractSokobanGame implements SokobanGame {

    protected final GameState state;
    private boolean isExitSpecified;

    /**
     * Creates an AbstractSokobanGame with the given game state.
     *
     * @param gameState the initial game state
     */
    protected AbstractSokobanGame(GameState gameState) {
        this.state = gameState;
        this.isExitSpecified = false;
    }

    /**
     * Processes an action and updates the game state.
     *
     * @param action the action to process
     * @return the result of processing
     */
    protected ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            isExitSpecified = true;
            return new Success(action);
        }
        return state.processAction(action);
    }

    /**
     * Returns whether the game loop should stop.
     *
     * @return true if the game should stop
     */
    protected boolean shouldStop() {
        return isExitSpecified || state.isWin();
    }

    /**
     * Returns whether an exit has been requested.
     *
     * @return true if exit was specified
     */
    protected boolean isExitSpecified() {
        return isExitSpecified;
    }
}
