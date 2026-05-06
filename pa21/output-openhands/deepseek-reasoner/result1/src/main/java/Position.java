import java.util.Objects;

/**
 * Represents a 2D grid position with row and column coordinates.
 */
public class Position {

    private final int row;
    private final int col;

    /**
     * Creates a position with the given row and column.
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
     * @return Row coordinate.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column coordinate.
     *
     * @return Column coordinate.
     */
    public int getCol() {
        return col;
    }

    /**
     * Creates a new position offset by the given row and column deltas.
     *
     * @param dRow Row delta.
     * @param dCol Column delta.
     * @return A new position offset by the given deltas.
     */
    public Position offsetBy(final int dRow, final int dCol) {
        return new Position(row + dRow, col + dCol);
    }

    /**
     * Creates a new position offset by the given offset.
     *
     * @param offset The offset to apply.
     * @return A new position offset by the given offset.
     */
    public Position offsetBy(final PositionOffset offset) {
        return new Position(row + offset.getDRow(), col + offset.getDCol());
    }

    /**
     * Creates a new position offset by the given row and column deltas,
     * or returns null if the resulting position is outside the board boundaries.
     *
     * @param dRow     Row delta.
     * @param dCol     Column delta.
     * @param numRows  Number of rows in the board.
     * @param numCols  Number of columns in the board.
     * @return A new position offset by the given deltas, or null if out of bounds.
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
     * Creates a new position offset by the given offset,
     * or returns null if the resulting position is outside the board boundaries.
     *
     * @param offset   The offset to apply.
     * @param numRows  Number of rows in the board.
     * @param numCols  Number of columns in the board.
     * @return A new position offset by the given offset, or null if out of bounds.
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
        if (o == null || getClass() != o.getClass()) {
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
