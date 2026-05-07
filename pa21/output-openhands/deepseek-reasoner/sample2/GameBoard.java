import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the game board containing cells and the player.
 */
public class GameBoard {

    private final int numRows;
    private final int numCols;
    private final Cell[][] board;
    private final Player player;

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
        this.board = new Cell[numRows][numCols];

        Player foundPlayer = null;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                this.board[r][c] = Objects.requireNonNull(board[r][c]);
                if (board[r][c] instanceof EntityCell) {
                    final Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity instanceof Player) {
                        foundPlayer = (Player) entity;
                    }
                }
            }
        }

        if (foundPlayer == null) {
            throw new IllegalStateException("Board must contain a Player");
        }
        this.player = foundPlayer;
    }

    /**
     * Returns the number of rows.
     *
     * @return Row count.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns the number of columns.
     *
     * @return Column count.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the player.
     *
     * @return The player entity.
     */
    public Player getPlayer() {
        return player;
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
     * Returns the cell at the given position.
     *
     * @param position The position.
     * @return The cell at the given position.
     */
    public Cell getCell(final Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Returns a copy of the row at the given index.
     *
     * @param row Row index.
     * @return An array of cells in the given row.
     */
    public Cell[] getRow(final int row) {
        return Arrays.copyOf(board[row], numCols);
    }
}
