import java.util.Objects;

/**
 * The game board containing all cells and the player.
 */
public class GameBoard {

    private final int numRows;
    private final int numCols;
    private final Cell[][] board;
    private final Player player;

    /**
     * Creates a game board with the specified dimensions and cell layout.
     *
     * @param numRows The number of rows.
     * @param numCols The number of columns.
     * @param board   The 2D array of cells.
     */
    public GameBoard(final int numRows, final int numCols, final Cell[][] board) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = Objects.requireNonNull(board);

        // Find the player on the board
        Player foundPlayer = null;
        for (int r = 0; r < numRows && foundPlayer == null; r++) {
            for (int c = 0; c < numCols && foundPlayer == null; c++) {
                if (board[r][c] instanceof EntityCell) {
                    final Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity instanceof Player) {
                        foundPlayer = (Player) entity;
                    }
                }
            }
        }
        this.player = Objects.requireNonNull(foundPlayer, "Board must contain a player");
    }

    /**
     * @return The number of rows on the board.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * @return The number of columns on the board.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the cell at the specified row and column.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The cell at the specified position.
     */
    public Cell getCell(final int row, final int col) {
        return board[row][col];
    }

    /**
     * Returns the cell at the specified position.
     *
     * @param position The position.
     * @return The cell at the specified position.
     */
    public Cell getCell(final Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Returns a copy of the specified row of cells.
     *
     * @param r The row index.
     * @return An array of cells in the specified row.
     */
    public Cell[] getRow(final int r) {
        final Cell[] rowCopy = new Cell[numCols];
        System.arraycopy(board[r], 0, rowCopy, 0, numCols);
        return rowCopy;
    }

    /**
     * @return The player entity.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Finds and returns the current position of the player on the board.
     *
     * @return The player's current position.
     * @throws IllegalStateException if the player is not found on the board.
     */
    public Position getPlayerPosition() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c] instanceof EntityCell) {
                    final Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity == player) {
                        return new Position(r, c);
                    }
                }
            }
        }
        throw new IllegalStateException("Player not found on the board");
    }

    /**
     * Moves the player from the specified source position to the destination position.
     *
     * @param from The source position.
     * @param to   The destination position.
     */
    public void movePlayer(final Position from, final Position to) {
        // Remove player from old cell
        final Cell fromCell = getCell(from);
        if (fromCell instanceof EntityCell) {
            ((EntityCell) fromCell).setEntity(null);
        }

        // Place player at new cell
        final Cell toCell = getCell(to);
        if (toCell instanceof StopCell) {
            ((StopCell) toCell).setPlayer(player);
        } else if (toCell instanceof EntityCell) {
            ((EntityCell) toCell).setEntity(player);
        }
    }

    /**
     * Checks whether the specified position is within the board boundaries.
     *
     * @param position The position to check.
     * @return {@code true} if the position is within bounds.
     */
    public boolean isValidPosition(final Position position) {
        final int r = position.getRow();
        final int c = position.getCol();
        return r >= 0 && r < numRows && c >= 0 && c < numCols;
    }

    /**
     * Counts the number of gems remaining on the board.
     *
     * @return The number of gems on the board.
     */
    public int countGems() {
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
