import java.util.Objects;

/**
 * The game board containing a grid of cells and a player.
 */
public class GameBoard {

    private final int numRows;
    private final int numCols;
    private final Cell[][] board;
    private final Player player;

    /**
     * Creates a game board with the given dimensions and cell layout.
     *
     * @param numRows The number of rows.
     * @param numCols The number of columns.
     * @param board   The 2D array of cells.
     */
    public GameBoard(final int numRows, final int numCols, final Cell[][] board) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = Objects.requireNonNull(board);
        this.player = findPlayer();
    }

    /**
     * Finds the player entity on the board.
     *
     * @return The player entity.
     */
    private Player findPlayer() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                final Cell cell = board[r][c];
                if (cell instanceof EntityCell) {
                    final Entity entity = ((EntityCell) cell).getEntity();
                    if (entity instanceof Player) {
                        return (Player) entity;
                    }
                }
            }
        }
        throw new IllegalStateException("No player found on the board");
    }

    /**
     * Returns the number of rows.
     *
     * @return The number of rows.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns the number of columns.
     *
     * @return The number of columns.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the cell at the given coordinates.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The cell at the given coordinates.
     */
    public Cell getCell(final int row, final int col) {
        return board[row][col];
    }

    /**
     * Returns the cell at the given position.
     *
     * @param position The position.
     * @return The cell at the given position.
     */
    public Cell getCell(final Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Returns the entire row at the given index.
     *
     * @param row The row index.
     * @return The array of cells in the row.
     */
    public Cell[] getRow(final int row) {
        return board[row];
    }

    /**
     * Returns the player entity.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the current position of the player on the board.
     *
     * @return The player's position.
     */
    public Position getPlayerPosition() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                final Cell cell = board[r][c];
                if (cell instanceof EntityCell) {
                    final Entity entity = ((EntityCell) cell).getEntity();
                    if (entity == player) {
                        return cell.getPosition();
                    }
                }
            }
        }
        throw new IllegalStateException("Player not found on board");
    }

    /**
     * Sets the player's position on the board, removing the player from the current cell
     * and placing them in the target cell.
     *
     * @param targetPosition The position to move the player to.
     */
    public void setPlayerPosition(final Position targetPosition) {
        // Remove player from current cell
        final Position currentPos = getPlayerPosition();
        final Cell currentCell = getCell(currentPos);
        if (currentCell instanceof EntityCell) {
            ((EntityCell) currentCell).setEntity(null);
        }

        // Place player in target cell
        final Cell targetCell = getCell(targetPosition);
        if (targetCell instanceof EntityCell) {
            ((EntityCell) targetCell).setEntity(player);
        } else {
            throw new IllegalStateException("Cannot place player in a Wall cell");
        }
    }

    /**
     * Returns the count of gems remaining on the board.
     *
     * @return The number of gems still on the board.
     */
    public int getRemainingGems() {
        int count = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                final Cell cell = board[r][c];
                if (cell instanceof EntityCell) {
                    final Entity entity = ((EntityCell) cell).getEntity();
                    if (entity instanceof Gem) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
