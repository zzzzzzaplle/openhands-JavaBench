import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for executing moves on the game board.
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
     * Executes a move in the given direction.
     * The player slides continuously until hitting a wall, the board boundary,
     * or entering a StopCell. Gems and ExtraLives are collected along the way.
     * Hitting a Mine results in a Dead result with position reverted.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult makeMove(final Direction direction) {
        final Position startPos = gameBoard.getPlayerPosition();
        Position currentPos = startPos;

        final List<Position> collectedGems = new ArrayList<>();
        final List<Position> collectedExtraLives = new ArrayList<>();

        while (true) {
            final Position nextPos = currentPos.offsetBy(direction.getOffset());

            // Check board boundary
            if (!gameBoard.isInBounds(nextPos)) {
                if (currentPos.equals(startPos)) {
                    return new Invalid(currentPos);
                }
                // Player stops at currentPos
                return new Alive(startPos, currentPos, collectedGems, collectedExtraLives);
            }

            final Cell nextCell = gameBoard.getCell(nextPos);

            // Check for wall - stop BEFORE the wall
            if (nextCell instanceof Wall) {
                if (currentPos.equals(startPos)) {
                    return new Invalid(currentPos);
                }
                return new Alive(startPos, currentPos, collectedGems, collectedExtraLives);
            }

            if (nextCell instanceof StopCell stopCell) {
                // Move player onto the stop cell and stop
                movePlayerTo(currentPos, nextPos);
                return new Alive(startPos, nextPos, collectedGems, collectedExtraLives);
            }

            if (nextCell instanceof EntityCell entityCell) {
                final Entity entity = entityCell.getEntity();

                if (entity instanceof Mine) {
                    // Revert player to original position
                    revertPlayerTo(startPos);
                    return new Dead(startPos, startPos);
                }

                if (entity instanceof Gem) {
                    collectedGems.add(nextPos);
                    entityCell.setEntity(null);
                } else if (entity instanceof ExtraLife) {
                    collectedExtraLives.add(nextPos);
                    entityCell.setEntity(null);
                }

                // Move player to this cell
                movePlayerTo(currentPos, nextPos);
                currentPos = nextPos;
                // Continue sliding
            }
        }
    }

    /**
     * Undoes a previous alive move, restoring the player position
     * and any collected entities.
     *
     * @param prevMove The previous alive move to undo.
     */
    public void undoMove(final Alive prevMove) {
        Objects.requireNonNull(prevMove);

        // Move player back to original position
        final Position currentPos = gameBoard.getPlayerPosition();
        movePlayerTo(currentPos, prevMove.getOrigPosition());

        // Restore collected gems
        for (final Position gemPos : prevMove.getCollectedGems()) {
            final Cell cell = gameBoard.getCell(gemPos);
            if (cell instanceof EntityCell entityCell) {
                entityCell.setEntity(new Gem());
            }
        }

        // Restore collected extra lives
        for (final Position lifePos : prevMove.getCollectedExtraLives()) {
            final Cell cell = gameBoard.getCell(lifePos);
            if (cell instanceof EntityCell entityCell) {
                entityCell.setEntity(new ExtraLife());
            }
        }
    }

    /**
     * Moves the player entity from one cell to another.
     *
     * @param from The source position.
     * @param to   The destination position.
     */
    private void movePlayerTo(final Position from, final Position to) {
        final Cell fromCell = gameBoard.getCell(from);
        final Cell toCell = gameBoard.getCell(to);

        if (fromCell instanceof EntityCell fromEC) {
            fromEC.setEntity(null);
        }

        final Player player = gameBoard.getPlayer();
        if (toCell instanceof EntityCell toEC) {
            toEC.setEntity(player);
        }
    }

    /**
     * Moves the player back to the given position (for Dead result).
     *
     * @param targetPos The target position for the player.
     */
    private void revertPlayerTo(final Position targetPos) {
        // Remove player from current cell
        final Position currentPos = gameBoard.getPlayerPosition();
        if (currentPos != null) {
            final Cell currentCell = gameBoard.getCell(currentPos);
            if (currentCell instanceof EntityCell ec) {
                ec.setEntity(null);
            }
        }

        // Place player at target position
        final Cell targetCell = gameBoard.getCell(targetPos);
        if (targetCell instanceof EntityCell ec) {
            ec.setEntity(gameBoard.getPlayer());
        }
    }
}
