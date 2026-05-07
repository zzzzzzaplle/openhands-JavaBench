import java.util.Objects;

/**
 * Represents an immutable position on a 2D grid.
 */
public class Position {

    private final int row;
    private final int col;

    /**
     * Creates a new position.
     *
     * @param row Row coordinate.
     * @param col Column coordinate.
     */
    public Position(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row coordinate.
     *
     * @return The row coordinate.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column coordinate.
     *
     * @return The column coordinate.
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns a new position offset by the given row and column deltas.
     *
     * @param dRow Row delta.
     * @param dCol Column delta.
     * @return A new offset position.
     */
    public Position offsetBy(final int dRow, final int dCol) {
        return new Position(row + dRow, col + dCol);
    }

    /**
     * Returns a new position offset by the given {@link PositionOffset}.
     *
     * @param offset The offset to apply.
     * @return A new offset position.
     */
    public Position offsetBy(final PositionOffset offset) {
        return new Position(row + offset.getDRow(), col + offset.getDCol());
    }

    /**
     * Returns a new position offset by the given deltas, or {@code null} if the resulting position
     * would be out of the specified bounds.
     *
     * @param dRow    Row delta.
     * @param dCol    Column delta.
     * @param numRows Number of rows in the grid (exclusive upper bound).
     * @param numCols Number of columns in the grid (exclusive upper bound).
     * @return An offset position, or {@code null} if out of bounds.
     */
    public Position offsetByOrNull(final int dRow, final int dCol,
                                   final int numRows, final int numCols) {
        final int newRow = row + dRow;
        final int newCol = col + dCol;
        if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {
            return null;
        }
        return new Position(newRow, newCol);
    }

    /**
     * Returns a new position offset by the given {@link PositionOffset}, or {@code null} if the
     * resulting position would be out of the specified bounds.
     *
     * @param offset  The offset to apply.
     * @param numRows Number of rows in the grid (exclusive upper bound).
     * @param numCols Number of columns in the grid (exclusive upper bound).
     * @return An offset position, or {@code null} if out of bounds.
     */
    public Position offsetByOrNull(final PositionOffset offset,
                                   final int numRows, final int numCols) {
        return offsetByOrNull(offset.getDRow(), offset.getDCol(), numRows, numCols);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        final Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
