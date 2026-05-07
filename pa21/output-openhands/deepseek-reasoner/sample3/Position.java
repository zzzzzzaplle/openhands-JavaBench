import java.util.Objects;

/**
 * Represents a position on the 2D game grid with row and column coordinates.
 */
public class Position {

    private final int row;
    private final int col;

    /**
     * Creates a position with the given row and column.
     *
     * @param row The row coordinate.
     * @param col The column coordinate.
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
     * Returns a new position offset from this position by the given row and column deltas.
     *
     * @param dRow The row delta.
     * @param dCol The column delta.
     * @return The new offset position.
     */
    public Position offsetBy(final int dRow, final int dCol) {
        return new Position(row + dRow, col + dCol);
    }

    /**
     * Returns a new position offset from this position by the given offset.
     *
     * @param offset The offset to apply.
     * @return The new offset position.
     */
    public Position offsetBy(final PositionOffset offset) {
        return new Position(row + offset.getDRow(), col + offset.getDCol());
    }

    /**
     * Returns a new position offset from this position, or {@code null} if the resulting position
     * is out of the grid bounds.
     *
     * @param dRow     The row delta.
     * @param dCol     The column delta.
     * @param numRows  The number of rows in the grid.
     * @param numCols  The number of columns in the grid.
     * @return The new offset position, or {@code null} if out of bounds.
     */
    public Position offsetByOrNull(final int dRow, final int dCol, final int numRows, final int numCols) {
        final int newRow = row + dRow;
        final int newCol = col + dCol;
        if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {
            return null;
        }
        return new Position(newRow, newCol);
    }

    /**
     * Returns a new position offset from this position by the given offset, or {@code null} if the
     * resulting position is out of the grid bounds.
     *
     * @param offset   The offset to apply.
     * @param numRows  The number of rows in the grid.
     * @param numCols  The number of columns in the grid.
     * @return The new offset position, or {@code null} if out of bounds.
     */
    public Position offsetByOrNull(final PositionOffset offset, final int numRows, final int numCols) {
        return offsetByOrNull(offset.getDRow(), offset.getDCol(), numRows, numCols);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Position position)) return false;
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
