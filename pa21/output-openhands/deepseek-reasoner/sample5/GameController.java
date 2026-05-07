import java.util.Objects;

/**
 * High-level controller that manages game logic including moves and undo operations.
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
     * Processes a move in the specified direction.
     * Only {@link MoveResult.Valid.Alive} results are pushed to the move stack.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult processMove(final Direction direction) {
        final MoveResult result = gameState.getGameBoardController().makeMove(direction);

        if (result instanceof MoveResult.Valid.Alive) {
            gameState.incrementNumMoves();
            gameState.getMoveStack().push((MoveResult.Valid.Alive) result);
        } else if (result instanceof MoveResult.Valid.Dead) {
            gameState.incrementNumMoves();
            gameState.incrementNumDeaths();
            gameState.decrementNumLives();
        }
        // Invalid moves are not tracked

        return result;
    }

    /**
     * Processes an undo of the last move.
     *
     * @return {@code true} if a move was undone, {@code false} if the stack was empty.
     */
    public boolean processUndo() {
        final MoveResult.Valid.Alive lastMove = gameState.getMoveStack().pop();
        if (lastMove == null) {
            return false;
        }

        gameState.getGameBoardController().undoMove(lastMove);
        return true;
    }
}
