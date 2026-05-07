import java.util.Objects;

/**
 * High-level game controller that coordinates game state and board operations.
 */
public class GameController {

    private final GameState gameState;
    private final GameBoardController boardController;

    /**
     * Creates a game controller for the given game state.
     *
     * @param gameState The game state to manage.
     */
    public GameController(final GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState);
        this.boardController = gameState.getGameBoardController();
    }

    /**
     * Processes a move in the given direction.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult processMove(final Direction direction) {
        final MoveResult result = boardController.makeMove(direction);

        if (result instanceof Alive) {
            final Alive alive = (Alive) result;
            gameState.getMoveStack().push(alive);
            gameState.incrementNumMoves();

            // Increase lives for collected extra lives
            for (final Position ignored : alive.getCollectedExtraLives()) {
                gameState.increaseNumLives(1);
            }
        } else if (result instanceof Dead) {
            gameState.incrementNumDeaths();
            gameState.decrementNumLives();
        }
        // Invalid: do nothing special

        return result;
    }

    /**
     * Processes an undo of the most recent move.
     *
     * @return {@code true} if a move was undone, {@code false} if the move stack is empty.
     */
    public boolean processUndo() {
        final Alive lastMove = gameState.getMoveStack().pop();
        if (lastMove == null) {
            return false;
        }

        boardController.undoMove(lastMove);
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
