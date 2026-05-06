import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the game board containing cells and a player.
 */
public class GameBoard {

    private final int numRows;
    private final int numCols;
    private final Cell[][] board;
    private Player player;

    /**
     * Creates a game board with the given dimensions and cell layout.
     *
     * @param numRows Number of rows.
     * @param numCols Number of columns.
     * @param board   The 2D array of cells.
     */
    public GameBoard(final int numRows, final int numCols, final Cell[][] board) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = Objects.requireNonNull(board);
        this.player = findPlayer(board);
    }

    /**
     * Finds the player entity in the board.
     *
     * @param board The 2D cell array to search.
     * @return The player found, or null.
     */
    private static Player findPlayer(final Cell[][] board) {
        for (final Cell[] row : board) {
            for (final Cell cell : row) {
                if (cell instanceof EntityCell entityCell) {
                    final Entity entity = entityCell.getEntity();
                    if (entity instanceof Player player) {
                        return player;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the number of rows.
     *
     * @return Number of rows.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns the number of columns.
     *
     * @return Number of columns.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the cell at the given position.
     *
     * @param position The position.
     * @return The cell at the position.
     */
    public Cell getCell(final Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Returns the cell at the given row and column.
     *
     * @param row Row index.
     * @param col Column index.
     * @return The cell at the given coordinates.
     */
    public Cell getCell(final int row, final int col) {
        return board[row][col];
    }

    /**
     * Returns an array of cells in the given row.
     *
     * @param row Row index.
     * @return Array of cells in the row.
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
     * Sets the player entity.
     *
     * @param player The player.
     */
    public void setPlayer(final Player player) {
        this.player = player;
    }

    /**
     * Returns the player's current position on the board.
     *
     * @return The player's position, or null if not found.
     */
    public Position getPlayerPosition() {
        if (player == null || player.getOwner() == null) {
            return null;
        }
        return player.getOwner().getPosition();
    }

    /**
     * Checks if the given position is within board boundaries.
     *
     * @param position The position to check.
     * @return True if the position is within bounds.
     */
    public boolean isInBounds(final Position position) {
        final int r = position.getRow();
        final int c = position.getCol();
        return r >= 0 && r < numRows && c >= 0 && c < numCols;
    }
}
