import java.util.Objects;

/**
 * Represents a delta offset in a 2D grid (row and column displacement).
 */
public class PositionOffset {

    private final int dRow;
    private final int dCol;

    /**
     * Creates a new offset.
     *
     * @param dRow Row displacement.
     * @param dCol Column displacement.
     */
    public PositionOffset(final int dRow, final int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    /**
     * Returns the row displacement.
     *
     * @return The row displacement.
     */
    public int getDRow() {
        return dRow;
    }

    /**
     * Returns the column displacement.
     *
     * @return The column displacement.
     */
    public int getDCol() {
        return dCol;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionOffset)) {
            return false;
        }
        final PositionOffset that = (PositionOffset) o;
        return dRow == that.dRow && dCol == that.dCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dRow, dCol);
    }

    @Override
    public String toString() {
        return "(" + dRow + ", " + dCol + ")";
    }
}
