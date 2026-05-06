import java.util.Objects;

/**
 * High-level controller that coordinates game logic between GameState and GameBoardController.
 */
public class GameController {

    private final GameState gameState;

    /**
     * Creates a game controller for the given game state.
     *
     * @param gameState The game state to control.
     */
    public GameController(final GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState);
    }

    /**
     * Processes a move in the given direction.
     * Updates game state (moves, deaths, lives) and pushes valid moves to the stack.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult processMove(final Direction direction) {
        final MoveResult result = gameState.getGameBoardController().makeMove(direction);

        if (result instanceof Alive alive) {
            gameState.incrementNumMoves();
            gameState.getMoveStack().push(alive);
        } else if (result instanceof Dead) {
            gameState.incrementNumMoves();
            gameState.incrementNumDeaths();
            gameState.decrementNumLives();
        }
        // Invalid: do nothing

        return result;
    }

    /**
     * Processes an undo request by popping the last move from the stack
     * and restoring the previous game state.
     *
     * @return True if an undo was performed, false if the stack was empty.
     */
    public boolean processUndo() {
        final Alive move = gameState.getMoveStack().pop();
        if (move == null) {
            return false;
        }
        gameState.getGameBoardController().undoMove(move);
        return true;
    }

    /**
     * Returns the game state.
     *
     * @return The game state.
     */
    public GameState getGameState() {
        return gameState;
    }
}
