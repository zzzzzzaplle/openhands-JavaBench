import java.util.Objects;

/**
 * An immutable offset representing a delta in row and column coordinates.
 */
public class PositionOffset {

    private final int dRow;
    private final int dCol;

    /**
     * Creates a position offset with the specified row and column deltas.
     *
     * @param dRow The row delta.
     * @param dCol The column delta.
     */
    public PositionOffset(final int dRow, final int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    /**
     * @return The row delta.
     */
    public int getDRow() {
        return dRow;
    }

    /**
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
        return "PositionOffset{" + "dRow=" + dRow + ", dCol=" + dCol + '}';
    }
}
