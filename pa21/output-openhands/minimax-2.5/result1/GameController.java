package game;

/**
 * Main game controller that handles moves and undo.
 */
public class GameController {
    private final GameState gameState;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    /**
     * Process a move in the given direction.
     * Returns the move result and updates game state.
     */
    public MoveResult processMove(Direction direction) {
        GameBoardController controller = gameState.getGameBoardController();
        MoveResult result = controller.makeMove(direction);

        if (result instanceof MoveResult.Valid) {
            gameState.incrementNumMoves();
            
            if (result instanceof MoveResult.Valid.Alive) {
                MoveResult.Valid.Alive alive = (MoveResult.Valid.Alive) result;
                
                // Update gems and extra lives collected
                for (Position gemPos : alive.getCollectedGems()) {
                    gameState.getGameBoard().removeEntity(gemPos);
                }
                for (Position lifePos : alive.getCollectedExtraLives()) {
                    gameState.getGameBoard().removeEntity(lifePos);
                    gameState.increaseNumLives(1);
                }
                
                // Push to move stack for undo (only Alive is undoable)
                gameState.getMoveStack().push(result);
            } else if (result instanceof MoveResult.Valid.Dead) {
                gameState.incrementNumDeaths();
                gameState.decrementNumLives();
            }
        }

        return result;
    }

    /**
     * Process an undo request.
     * Returns true if undo was successful.
     */
    public boolean processUndo() {
        MoveStack stack = gameState.getMoveStack();
        
        if (stack.isEmpty()) {
            return false;
        }

        MoveResult prevMove = stack.pop();
        
        if (prevMove instanceof MoveResult.Valid) {
            GameBoardController controller = gameState.getGameBoardController();
            controller.undoMove(prevMove);
            return true;
        }

        return false;
    }
}