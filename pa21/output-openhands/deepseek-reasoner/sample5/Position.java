import java.util.Objects;

/**
 * An immutable position on the game board, specified by row and column coordinates.
 */
public class Position {

    private final int row;
    private final int col;

    /**
     * Creates a position with the specified row and column.
     *
     * @param row The row coordinate.
     * @param col The column coordinate.
     */
    public Position(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return The row coordinate.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The column coordinate.
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns a new position offset by the specified row and column deltas.
     *
     * @param dRow The row delta.
     * @param dCol The column delta.
     * @return A new position offset by {@code dRow} and {@code dCol}.
     */
    public Position offsetBy(final int dRow, final int dCol) {
        return new Position(row + dRow, col + dCol);
    }

    /**
     * Returns a new position offset by the specified offset.
     *
     * @param offset The offset to apply.
     * @return A new position offset by {@code offset}.
     */
    public Position offsetBy(final PositionOffset offset) {
        return new Position(row + offset.getDRow(), col + offset.getDCol());
    }

    /**
     * Returns a new position offset by the specified deltas, or {@code null} if the resulting
     * position is outside the board boundaries.
     *
     * @param dRow    The row delta.
     * @param dCol    The column delta.
     * @param numRows The number of rows on the board.
     * @param numCols The number of columns on the board.
     * @return A new position offset by {@code dRow} and {@code dCol}, or {@code null} if out of bounds.
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
     * Returns a new position offset by the specified offset, or {@code null} if the resulting
     * position is outside the board boundaries.
     *
     * @param offset  The offset to apply.
     * @param numRows The number of rows on the board.
     * @param numCols The number of columns on the board.
     * @return A new position offset by {@code offset}, or {@code null} if out of bounds.
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
        return "Position{" + "row=" + row + ", col=" + col + '}';
    }
}
