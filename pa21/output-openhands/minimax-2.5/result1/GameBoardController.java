package game;

/**
 * Controller for handling moves on the game board.
 */
public class GameBoardController {
    private final GameBoard gameBoard;

    public GameBoardController(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Make a move in the given direction.
     * Player slides until hitting a Wall, boundary, or StopCell.
     */
    public MoveResult makeMove(Direction direction) {
        Position origPos = gameBoard.getPlayerPosition();
        if (origPos == null) {
            return new MoveResult.Invalid();
        }

        Position currentPos = origPos.clone();
        PositionOffset offset = direction.getOffset();
        int numRows = gameBoard.getNumRows();
        int numCols = gameBoard.getNumCols();

        // Try first step to see if we can move at all
        Position nextPos = currentPos.offsetByOrNull(offset, numRows, numCols);
        if (nextPos == null) {
            return new MoveResult.Invalid();
        }

        Cell cell = gameBoard.getCell(nextPos);
        if (cell instanceof Wall) {
            return new MoveResult.Invalid();
        }

        // Track collected items
        MoveResult.Valid.Alive aliveResult = new MoveResult.Valid.Alive(origPos, nextPos);

        // Slide until we hit something
        boolean moved = false;
        while (true) {
            cell = gameBoard.getCell(nextPos);
            
            // Check for mine - Dead result
            if (cell instanceof EntityCell) {
                Entity entity = ((EntityCell) cell).getEntity();
                if (entity instanceof Mine) {
                    // Hit mine - Dead
                    MoveResult.Valid.Dead deadResult = new MoveResult.Valid.Dead(origPos, nextPos);
                    return deadResult;
                }
            }

            // Move to next position
            currentPos = nextPos;
            moved = true;

            // Collect items at new position
            collectItems(currentPos, aliveResult);

            // Check if we've landed on a StopCell - stop here
            if (gameBoard.getCell(currentPos) instanceof StopCell) {
                break;
            }

            // Try to move to next position
            nextPos = currentPos.offsetByOrNull(offset, numRows, numCols);
            if (nextPos == null) {
                // Hit boundary - stop here
                break;
            }

            cell = gameBoard.getCell(nextPos);
            if (cell instanceof Wall) {
                // Would hit wall - stop at current position
                break;
            }
        }

        if (!moved) {
            return new MoveResult.Invalid();
        }

        // Update player position
        gameBoard.setEntity(currentPos, gameBoard.getPlayer());
        gameBoard.removeEntity(origPos);

        aliveResult.newPosition = currentPos;
        return aliveResult;
    }

    /**
     * Collect gems and extra lives at the given position.
     */
    private void collectItems(Position pos, MoveResult.Valid.Alive result) {
        Cell cell = gameBoard.getCell(pos);
        if (cell instanceof EntityCell) {
            Entity entity = ((EntityCell) cell).getEntity();
            if (entity instanceof Gem) {
                result.addCollectedGem(pos);
                gameBoard.removeEntity(pos);
            } else if (entity instanceof ExtraLife) {
                result.addCollectedExtraLife(pos);
                gameBoard.removeEntity(pos);
            }
        }
    }

    /**
     * Undo a previous move.
     */
    public void undoMove(MoveResult prevMove) {
        if (prevMove instanceof MoveResult.Valid) {
            MoveResult.Valid validMove = (MoveResult.Valid) prevMove;
            Position origPos = validMove.getOrigPosition();
            Position newPos = validMove.getNewPosition();

            // Move player back to original position
            gameBoard.setEntity(origPos, gameBoard.getPlayer());
            gameBoard.removeEntity(newPos);
        }
    }
}