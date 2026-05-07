import java.util.Objects;

/**
 * Represents a delta (offset) in a 2D grid, defined by row and column changes.
 */
public class PositionOffset {

    private final int dRow;
    private final int dCol;

    /**
     * Creates a new offset with the specified row and column deltas.
     *
     * @param dRow The row delta.
     * @param dCol The column delta.
     */
    public PositionOffset(final int dRow, final int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    /**
     * Returns the row delta.
     *
     * @return The row delta.
     */
    public int getDRow() {
        return dRow;
    }

    /**
     * Returns the column delta.
     *
     * @return The column delta.
     */
    public int getDCol() {
        return dCol;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PositionOffset that = (PositionOffset) o;
        return dRow == that.dRow && dCol == that.dCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dRow, dCol);
    }
}
