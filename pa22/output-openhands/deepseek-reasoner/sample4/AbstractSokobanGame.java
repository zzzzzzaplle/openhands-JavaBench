/**
 * Base class for Sokoban game implementations containing shared game logic.
 */
public abstract class AbstractSokobanGame implements SokobanGame {

    protected final GameState state;
    private boolean exitRequested;

    /**
     * Create an abstract Sokoban game with the given game state.
     *
     * @param gameState the initial game state
     */
    protected AbstractSokobanGame(GameState gameState) {
        this.state = gameState;
        this.exitRequested = false;
    }

    /**
     * @return true if the game loop should stop
     */
    protected boolean shouldStop() {
        return exitRequested || state.isWin();
    }

    /**
     * Process an action and return the result.
     *
     * @param action the action to process
     * @return the result of processing the action
     */
    protected ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            exitRequested = true;
            return new Success(action);
        }

        if (action instanceof Move move) {
            return state.processMove(move);
        }

        if (action instanceof Undo undo) {
            return state.processUndo(undo);
        }

        if (action instanceof InvalidInput invalid) {
            return new Failed(action, invalid.getMessage());
        }

        return new Failed(action, "Unknown action");
    }

    /**
     * @return true if exit has been requested
     */
    protected boolean isExitRequested() {
        return exitRequested;
    }
}
