import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controls movement logic on the game board.
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
     * Attempts to make a move in the given direction.
     * <p>
     * The player slides step-by-step until hitting a wall, the board boundary, or landing on a
     * StopCell. Gems and ExtraLives are collected along the way. Hitting a Mine results in a
     * {@link Dead} result and the player is reverted to the original position.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult makeMove(final Direction direction) {
        Objects.requireNonNull(direction);

        final Player player = gameBoard.getPlayer();
        final Position origPos = player.getOwner().getPosition();
        Position currentPos = origPos;

        final List<Position> collectedGems = new ArrayList<>();
        final List<Position> collectedExtraLives = new ArrayList<>();
        boolean moved = false;

        final PositionOffset offset = direction.getOffset();

        while (true) {
            final Position nextPos = currentPos.offsetBy(offset);

            // Stop before board boundary
            if (nextPos.getRow() < 0 || nextPos.getRow() >= gameBoard.getNumRows()
                    || nextPos.getCol() < 0 || nextPos.getCol() >= gameBoard.getNumCols()) {
                break;
            }

            final Cell nextCell = gameBoard.getCell(nextPos);

            // Stop immediately before a Wall
            if (nextCell instanceof Wall) {
                break;
            }

            if (nextCell instanceof StopCell) {
                // Stop exactly on a StopCell
                moved = true;
                currentPos = nextPos;
                break;
            }

            if (nextCell instanceof EntityCell) {
                final EntityCell entityCell = (EntityCell) nextCell;
                final Entity entity = entityCell.getEntity();

                if (entity instanceof Mine) {
                    // Dead - player position is reverted to original
                    return new Dead(origPos, origPos);
                }

                if (entity instanceof Gem) {
                    collectedGems.add(nextPos);
                    entityCell.setEntity(null);
                    moved = true;
                    currentPos = nextPos;
                    continue;
                }

                if (entity instanceof ExtraLife) {
                    collectedExtraLives.add(nextPos);
                    entityCell.setEntity(null);
                    moved = true;
                    currentPos = nextPos;
                    continue;
                }

                // Empty entity cell - slide through
                moved = true;
                currentPos = nextPos;
                continue;
            }

            // Unknown cell type - stop
            break;
        }

        if (!moved) {
            return new Invalid(origPos);
        }

        // Remove player from original position
        final Cell origCell = gameBoard.getCell(origPos);
        if (origCell instanceof EntityCell) {
            ((EntityCell) origCell).setEntity(null);
        }

        // Place player at final position
        final Cell finalCell = gameBoard.getCell(currentPos);
        if (finalCell instanceof StopCell) {
            ((StopCell) finalCell).setPlayer(player);
        } else {
            ((EntityCell) finalCell).setEntity(player);
        }

        return new Alive(origPos, currentPos, collectedGems, collectedExtraLives);
    }

    /**
     * Undoes a previous valid move by restoring the player to the original position and
     * recreating any collected entities.
     *
     * @param prevMove The previous move to undo.
     */
    public void undoMove(final MoveResult prevMove) {
        Objects.requireNonNull(prevMove);

        if (!(prevMove instanceof Valid)) {
            return;
        }

        final Valid validMove = (Valid) prevMove;
        final Position origPos = validMove.getOrigPosition();
        final Position newPos = validMove.getNewPosition();

        final Player player = gameBoard.getPlayer();

        // Remove player from current position
        final Cell currentCell = gameBoard.getCell(newPos);
        if (currentCell instanceof EntityCell) {
            ((EntityCell) currentCell).setEntity(null);
        }

        // Restore player to original position
        final Cell origCell = gameBoard.getCell(origPos);
        if (origCell instanceof StopCell) {
            ((StopCell) origCell).setPlayer(player);
        } else {
            ((EntityCell) origCell).setEntity(player);
        }

        // Restore collected entities
        if (validMove instanceof Alive) {
            final Alive aliveMove = (Alive) validMove;
            for (final Position gemPos : aliveMove.getCollectedGems()) {
                final Cell gemCell = gameBoard.getCell(gemPos);
                if (gemCell instanceof EntityCell) {
                    ((EntityCell) gemCell).setEntity(new Gem());
                }
            }
            for (final Position lifePos : aliveMove.getCollectedExtraLives()) {
                final Cell lifeCell = gameBoard.getCell(lifePos);
                if (lifeCell instanceof EntityCell) {
                    ((EntityCell) lifeCell).setEntity(new ExtraLife());
                }
            }
        }
    }
}
