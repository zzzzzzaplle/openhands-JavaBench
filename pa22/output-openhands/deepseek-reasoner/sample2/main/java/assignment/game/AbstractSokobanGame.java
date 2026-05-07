package assignment.game;

/**
 * Abstract base class for Sokoban game implementations.
 * Provides shared game logic including action dispatching and
 * termination condition checking.
 */
public abstract class AbstractSokobanGame implements SokobanGame {

    /** The current game state. */
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
     * Returns whether the game loop should stop.
     *
     * @return true if the game should stop (exit was specified or the game is won)
     */
    protected boolean shouldStop() {
        return isExitSpecified || state.isWin();
    }

    /**
     * Processes a game action and returns the result.
     *
     * @param action the action to process
     * @return the result of processing the action
     */
    protected ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            isExitSpecified = true;
            return new Success(action);
        }
        if (action instanceof Undo undo) {
            return state.processUndo(undo);
        }
        if (action instanceof Move move) {
            return state.processMove(move);
        }
        if (action instanceof InvalidInput) {
            return new Failed(action, StringResources.INVALID_INPUT_MESSAGE);
        }
        throw new ShouldNotReachException();
    }
}
