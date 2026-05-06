package assignment.game;

/**
 * Abstract base class for Sokoban game implementations.
 * Manages game state, action processing, and win/exit conditions.
 */
public abstract class AbstractSokobanGame implements SokobanGame {
    protected final GameState state;
    private boolean isExitSpecified;

    /**
     * Create an abstract Sokoban game with the given game state.
     *
     * @param gameState the initial game state
     */
    protected AbstractSokobanGame(GameState gameState) {
        this.state = gameState;
        this.isExitSpecified = false;
    }

    /**
     * @return true if the game should stop (exit specified or game won)
     */
    protected boolean shouldStop() {
        return isExitSpecified || state.isWin();
    }

    /**
     * Process an action and return the result.
     *
     * @param action the action to process
     * @return the result of processing the action
     */
    protected ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            isExitSpecified = true;
            return new Success(action);
        } else if (action instanceof InvalidInput) {
            return new Failed(action, ((InvalidInput) action).getMessage());
        } else if (action instanceof Move) {
            return state.processMove((Move) action);
        } else if (action instanceof Undo) {
            return state.processUndo((Undo) action);
        }
        return new Failed(action, "Unknown action");
    }
}
