import java.util.Objects;

/**
 * High-level controller that coordinates game state updates with board movement.
 */
public class GameController {

    private final GameState gameState;

    /**
     * Creates a controller for the given game state.
     *
     * @param gameState The game state to control.
     */
    public GameController(final GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState);
    }

    /**
     * Processes a move in the given direction.
     * <p>
     * Delegates to the board controller and updates game state accordingly.
     * Only {@link Alive} results are pushed to the move stack and counted as moves.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult processMove(final Direction direction) {
        Objects.requireNonNull(direction);

        final MoveResult result = gameState.getGameBoardController().makeMove(direction);

        if (result instanceof Alive) {
            final Alive alive = (Alive) result;
            gameState.getMoveStack().push(alive);
            gameState.incrementNumMoves();
            if (!alive.getCollectedGems().isEmpty()) {
                gameState.addGemsCollected(alive.getCollectedGems().size());
            }
            for (final Position ignored : alive.getCollectedExtraLives()) {
                gameState.increaseNumLives(1);
            }
        } else if (result instanceof Dead) {
            gameState.incrementNumDeaths();
            gameState.decrementNumLives();
        }

        return result;
    }

    /**
     * Processes an undo request.
     * <p>
     * Pops the most recent {@link Alive} move from the stack and reverts the board and game
     * state accordingly.
     *
     * @return {@code true} if an undo was performed, {@code false} if the stack was empty.
     */
    public boolean processUndo() {
        if (gameState.getMoveStack().isEmpty()) {
            return false;
        }

        final Alive move = gameState.getMoveStack().pop();
        gameState.getGameBoardController().undoMove(move);

        // Revert collected gems
        if (!move.getCollectedGems().isEmpty()) {
            gameState.removeGemsCollected(move.getCollectedGems().size());
        }

        // Revert collected extra lives
        for (final Position ignored : move.getCollectedExtraLives()) {
            gameState.decreaseNumLives(1);
        }

        return true;
    }

    /**
     * Returns the underlying game state.
     *
     * @return The game state.
     */
    public GameState getGameState() {
        return gameState;
    }
}
