/**
 * Represents the game board containing all cells and the player.
 */
public class GameBoard {

    private final int numRows;
    private final int numCols;
    private final Cell[][] board;
    private Player player;

    /**
     * Creates a game board from the given 2D cell array.
     *
     * @param numRows The number of rows.
     * @param numCols The number of columns.
     * @param board   The 2D array of cells.
     */
    public GameBoard(final int numRows, final int numCols, final Cell[][] board) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = board;
        this.player = findPlayer();
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
     * Returns the cell at the specified row and column.
     *
     * @param row The row.
     * @param col The column.
     * @return The cell at {@code (row, col)}.
     */
    public Cell getCell(final int row, final int col) {
        return board[row][col];
    }

    /**
     * Returns the cell at the specified position.
     *
     * @param position The position.
     * @return The cell at {@code position}.
     */
    public Cell getCell(final Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Returns a copy of the row at the specified index.
     *
     * @param row The row index.
     * @return An array of cells in the specified row.
     */
    public Cell[] getRow(final int row) {
        return board[row].clone();
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
     * Finds the player on the board by scanning all cells.
     *
     * @return The player entity, or {@code null} if not found.
     */
    private Player findPlayer() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c] instanceof EntityCell) {
                    final Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity instanceof Player) {
                        return (Player) entity;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds the position of the player on the board.
     *
     * @return The player's position, or {@code null} if not found.
     */
    public Position getPlayerPosition() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c] instanceof EntityCell) {
                    final Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity instanceof Player) {
                        return new Position(r, c);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Moves the player from the source position to the destination position.
     * The player entity is removed from the source cell and placed in the destination cell.
     *
     * @param from The source position.
     * @param to   The destination position.
     */
    public void movePlayer(final Position from, final Position to) {
        final Cell fromCell = getCell(from);
        final Cell toCell = getCell(to);

        if (!(fromCell instanceof EntityCell)) {
            throw new IllegalStateException("Player is not in an EntityCell at " + from);
        }

        final Entity entity = ((EntityCell) fromCell).removeEntity();
        if (!(entity instanceof Player)) {
            // Restore and throw
            ((EntityCell) fromCell).setEntity(entity);
            throw new IllegalStateException("No player found at " + from);
        }

        if (toCell instanceof EntityCell) {
            ((EntityCell) toCell).setEntity(entity);
        } else {
            throw new IllegalStateException("Cannot move player to cell type: " + toCell.getClass().getSimpleName());
        }
    }

    /**
     * Returns the number of gems currently on the board.
     *
     * @return The count of gems.
     */
    public int getNumGems() {
        int count = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c] instanceof EntityCell) {
                    final Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity instanceof Gem) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
