import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controls movement and undo operations on the game board.
 */
public class GameBoardController {

    private final GameBoard gameBoard;

    /**
     * Creates a controller for the specified game board.
     *
     * @param gameBoard The game board to control.
     */
    public GameBoardController(final GameBoard gameBoard) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
    }

    /**
     * Processes a move in the specified direction.
     * The player slides continuously until hitting a wall, the board boundary, or a StopCell.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult makeMove(final Direction direction) {
        final PositionOffset offset = direction.getOffset();
        final Position origPos = gameBoard.getPlayerPosition();

        // Calculate first step
        Position nextPos = origPos.offsetBy(offset);

        // Check if the first step is blocked (invalid move)
        if (!gameBoard.isValidPosition(nextPos)) {
            return new MoveResult.Invalid(origPos);
        }

        Cell cell = gameBoard.getCell(nextPos);
        if (cell instanceof Wall) {
            return new MoveResult.Invalid(origPos);
        }

        // Slide step by step
        Position currentPos = origPos;
        final List<Position> collectedGems = new ArrayList<>();
        final List<Position> collectedExtraLives = new ArrayList<>();
        boolean hitMine = false;

        while (true) {
            if (!gameBoard.isValidPosition(nextPos)) {
                // Stop at the last valid position (before boundary)
                break;
            }

            cell = gameBoard.getCell(nextPos);

            if (cell instanceof Wall) {
                // Stop at the last valid position (before wall)
                break;
            }

            // Advance to the next position
            currentPos = nextPos;

            // Check what's in the current cell
            if (cell instanceof EntityCell) {
                final EntityCell entityCell = (EntityCell) cell;
                final Entity entity = entityCell.getEntity();

                if (entity instanceof Gem) {
                    collectedGems.add(currentPos);
                    entityCell.setEntity(null);
                } else if (entity instanceof ExtraLife) {
                    collectedExtraLives.add(currentPos);
                    entityCell.setEntity(null);
                } else if (entity instanceof Mine) {
                    hitMine = true;
                    break;
                }
                // If entity instanceof Player (at StopCell), we continue sliding through
            }

            if (cell instanceof StopCell) {
                // Stop exactly on StopCell
                break;
            }

            // Calculate next position
            nextPos = currentPos.offsetBy(offset);
        }

        if (hitMine) {
            // Player dies, stays at original position
            return new MoveResult.Valid.Dead(origPos);
        }

        // Move player to final position
        if (!currentPos.equals(origPos)) {
            gameBoard.movePlayer(origPos, currentPos);
        }

        return new MoveResult.Valid.Alive(currentPos, origPos, collectedGems, collectedExtraLives);
    }

    /**
     * Undoes a previous alive move, restoring the player and collected entities.
     *
     * @param prevMove The move to undo.
     */
    public void undoMove(final MoveResult.Valid.Alive prevMove) {
        // Move player back to original position
        final Position currentPos = gameBoard.getPlayerPosition();
        gameBoard.movePlayer(currentPos, prevMove.getOrigPosition());

        // Restore collected gems
        for (final Position gemPos : prevMove.getCollectedGems()) {
            final Cell cell = gameBoard.getCell(gemPos);
            if (cell instanceof EntityCell) {
                ((EntityCell) cell).setEntity(new Gem());
            }
        }

        // Restore collected extra lives
        for (final Position lifePos : prevMove.getCollectedExtraLives()) {
            final Cell cell = gameBoard.getCell(lifePos);
            if (cell instanceof EntityCell) {
                ((EntityCell) cell).setEntity(new ExtraLife());
            }
        }
    }
}
