import java.util.Objects;

/**
 * High-level game controller that coordinates game state updates with move execution.
 */
public class GameController {

    private final GameState gameState;

    /**
     * Creates a game controller for the specified game state.
     *
     * @param gameState The game state to manage.
     */
    public GameController(final GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState);
    }

    /**
     * Processes a move in the given direction.
     * <p>
     * Updates game state counters and the move stack based on the result.
     * Only {@link Alive} results are pushed to the move stack.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult processMove(final Direction direction) {
        final GameBoardController controller = gameState.getGameBoardController();
        final MoveResult result = controller.makeMove(direction);

        if (result instanceof Alive) {
            gameState.incrementNumMoves();
            gameState.getMoveStack().push((Alive) result);

            // Handle collected extra lives
            final Alive alive = (Alive) result;
            if (!alive.getCollectedExtraLives().isEmpty()) {
                gameState.increaseNumLives(alive.getCollectedExtraLives().size());
            }
        } else if (result instanceof Dead) {
            gameState.incrementNumDeaths();
            gameState.decrementNumLives();
        }
        // Invalid: no state changes

        return result;
    }

    /**
     * Processes an undo operation.
     * <p>
     * Pops the most recent {@link Alive} move from the stack and reverts it.
     *
     * @return {@code true} if an undo was performed, {@code false} if the stack was empty.
     */
    public boolean processUndo() {
        final Alive lastMove = gameState.getMoveStack().pop();
        if (lastMove == null) {
            return false;
        }

        final GameBoardController controller = gameState.getGameBoardController();
        controller.undoMove(lastMove);

        // Revert lives gained from collected extra lives during this move
        if (!lastMove.getCollectedExtraLives().isEmpty()) {
            gameState.decreaseNumLives(lastMove.getCollectedExtraLives().size());
        }

        return true;
    }

    /**
     * Returns the game state managed by this controller.
     *
     * @return The game state.
     */
    public GameState getGameState() {
        return gameState;
    }
}
