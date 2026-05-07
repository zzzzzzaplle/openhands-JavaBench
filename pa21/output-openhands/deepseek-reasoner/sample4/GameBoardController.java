import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controls the game board, handling move execution and undo.
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
     * Executes a move in the specified direction.
     * <p>
     * The player slides continuously until hitting a wall, the board boundary,
     * or landing on a StopCell. Gems and ExtraLives are collected along the way.
     * Hitting a Mine results in death.
     *
     * @param direction The direction to move.
     * @return The result of the move.
     */
    public MoveResult makeMove(final Direction direction) {
        final PositionOffset offset = direction.getOffset();
        final Position currentPos = gameBoard.getPlayerPosition();

        if (currentPos == null) {
            throw new IllegalStateException("Player not found on the board");
        }

        // Check if the first step is valid
        final Position firstStep = currentPos.offsetByOrNull(offset, gameBoard.getNumRows(), gameBoard.getNumCols());
        if (firstStep == null) {
            return new Invalid(currentPos);
        }

        final Cell firstCell = gameBoard.getCell(firstStep);
        if (firstCell instanceof Wall) {
            return new Invalid(currentPos);
        }

        // Slide step by step
        final List<Position> collectedGems = new ArrayList<>();
        final List<Position> collectedExtraLives = new ArrayList<>();
        Position slidePos = currentPos;

        while (true) {
            final Position tryPos = slidePos.offsetByOrNull(offset, gameBoard.getNumRows(), gameBoard.getNumCols());
            if (tryPos == null) {
                // Reached the boundary - stop before it
                break;
            }

            final Cell cell = gameBoard.getCell(tryPos);
            if (cell instanceof Wall) {
                // Stop before the wall
                break;
            }

            if (cell instanceof StopCell) {
                // Move player onto the StopCell
                gameBoard.movePlayer(currentPos, tryPos);
                return new Alive(tryPos, currentPos, collectedGems, collectedExtraLives);
            }

            if (cell instanceof EntityCell) {
                final Entity entity = ((EntityCell) cell).getEntity();
                if (entity instanceof Mine) {
                    // Dead - player doesn't move, position reverts to original
                    return new Dead(currentPos, currentPos);
                }
                if (entity instanceof Gem) {
                    collectedGems.add(tryPos);
                    ((EntityCell) cell).removeEntity();
                }
                if (entity instanceof ExtraLife) {
                    collectedExtraLives.add(tryPos);
                    ((EntityCell) cell).removeEntity();
                }
            }

            slidePos = tryPos;
        }

        // Move player to final slide position
        if (!slidePos.equals(currentPos)) {
            gameBoard.movePlayer(currentPos, slidePos);
        }
        return new Alive(slidePos, currentPos, collectedGems, collectedExtraLives);
    }

    /**
     * Undoes a previous move, restoring the player and any collected entities.
     *
     * @param prevMove The previous move to undo. Expected to be an {@link Alive} result.
     */
    public void undoMove(final MoveResult prevMove) {
        if (!(prevMove instanceof Alive)) {
            return;
        }

        final Alive aliveMove = (Alive) prevMove;
        final Position currentPlayerPos = gameBoard.getPlayerPosition();

        // Move player back to original position
        if (!currentPlayerPos.equals(aliveMove.getOrigPosition())) {
            gameBoard.movePlayer(currentPlayerPos, aliveMove.getOrigPosition());
        }

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
    }
}
