import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controls the game board, handling player movement and undo operations.
 */
public class GameBoardController {

    private final GameBoard gameBoard;

    /**
     * Creates a controller for the given game board.
     *
     * @param gameBoard The game board to control.
     */
    public GameBoardController(final GameBoard gameBoard) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
    }

    /**
     * Attempts to move the player in the given direction using inertial sliding.
     *
     * @param direction The direction to move in.
     * @return The result of the move.
     */
    public MoveResult makeMove(final Direction direction) {
        final Position origPos = gameBoard.getPlayerPosition();
        Position currPos = origPos;
        final List<Position> collectedGems = new ArrayList<>();
        final List<Position> collectedExtraLives = new ArrayList<>();
        boolean moved = false;

        final int numRows = gameBoard.getNumRows();
        final int numCols = gameBoard.getNumCols();
        final PositionOffset offset = direction.getOffset();

        while (true) {
            final Position nextPos = currPos.offsetByOrNull(offset, numRows, numCols);

            // Stop at board boundary
            if (nextPos == null) {
                break;
            }

            final Cell nextCell = gameBoard.getCell(nextPos);

            // Stop before a wall
            if (nextCell instanceof Wall) {
                break;
            }

            // nextCell is an EntityCell (or StopCell)
            final EntityCell entityCell = (EntityCell) nextCell;
            final Entity entity = entityCell.getEntity();
            moved = true;

            // Hit a mine
            if (entity instanceof Mine) {
                return new Dead(origPos, nextPos);
            }

            // Collect gem
            if (entity instanceof Gem) {
                collectedGems.add(nextPos);
                entityCell.setEntity(null);
            }

            // Collect extra life
            if (entity instanceof ExtraLife) {
                collectedExtraLives.add(nextPos);
                entityCell.setEntity(null);
            }

            // Move player to this cell
            currPos = nextPos;

            // Stop exactly on a StopCell
            if (nextCell instanceof StopCell) {
                break;
            }
        }

        // Player could not move at all
        if (!moved) {
            return new Invalid(origPos);
        }

        // Update the player's actual position on the board
        gameBoard.setPlayerPosition(currPos);

        return new Alive(origPos, currPos, collectedGems, collectedExtraLives);
    }

    /**
     * Undoes a previous move, restoring the player position and any collected entities.
     *
     * @param prevMove The previous alive move result to undo.
     */
    public void undoMove(final MoveResult prevMove) {
        final Alive aliveMove = (Alive) prevMove;

        // Restore collected gems
        for (final Position gemPos : aliveMove.getCollectedGems()) {
            final Cell cell = gameBoard.getCell(gemPos);
            if (cell instanceof EntityCell) {
                ((EntityCell) cell).setEntity(new Gem());
            }
        }

        // Restore collected extra lives
        for (final Position lifePos : aliveMove.getCollectedExtraLives()) {
            final Cell cell = gameBoard.getCell(lifePos);
            if (cell instanceof EntityCell) {
                ((EntityCell) cell).setEntity(new ExtraLife());
            }
        }

        // Move the player back to the original position
        gameBoard.setPlayerPosition(aliveMove.getOrigPosition());
    }
}
