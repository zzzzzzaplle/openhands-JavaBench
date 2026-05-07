/**
 * Abstract base class for Sokoban game implementations. Manages the core game
 * loop logic including checking for stop conditions (win or exit) and
 * processing actions.
 */
public abstract class AbstractSokobanGame implements SokobanGame {

    protected final GameState state;
    private boolean isExitSpecified;

    /**
     * Create a new abstract Sokoban game with the given state.
     *
     * @param gameState the initial game state
     */
    protected AbstractSokobanGame(GameState gameState) {
        this.state = gameState;
        this.isExitSpecified = false;
    }

    /**
     * Check whether the game loop should stop.
     *
     * @return true if the game should stop (exit requested or win condition met)
     */
    protected boolean shouldStop() {
        return isExitSpecified || state.isWin();
    }

    /**
     * Process an action, handling exit specially and delegating other actions
     * to the game state.
     *
     * @param action the action to process
     * @return the result of processing the action
     */
    protected ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            isExitSpecified = true;
            return new Success(action);
        }
        return state.processAction(action);
    }
}
