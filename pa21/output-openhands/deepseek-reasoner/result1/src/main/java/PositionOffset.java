import java.util.Objects;

/**
 * Represents a delta (offset) for row and column coordinates.
 */
public class PositionOffset {

    private final int dRow;
    private final int dCol;

    /**
     * Creates a position offset with the given row and column deltas.
     *
     * @param dRow Row delta.
     * @param dCol Column delta.
     */
    public PositionOffset(final int dRow, final int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    /**
     * Returns the row delta.
     *
     * @return Row delta.
     */
    public int getDRow() {
        return dRow;
    }

    /**
     * Returns the column delta.
     *
     * @return Column delta.
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
