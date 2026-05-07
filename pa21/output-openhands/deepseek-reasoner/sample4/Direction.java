/**
 * Enum representing the four cardinal directions for movement on the grid.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowOffset;
    private final int colOffset;

    /**
     * Creates a direction with the specified row and column offsets.
     *
     * @param rowOffset The row offset.
     * @param colOffset The column offset.
     */
    Direction(final int rowOffset, final int colOffset) {
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    /**
     * Returns a {@link PositionOffset} representing this direction.
     *
     * @return A {@link PositionOffset} for this direction.
     */
    public PositionOffset getOffset() {
        return new PositionOffset(rowOffset, colOffset);
    }

    /**
     * Returns the row offset of this direction.
     *
     * @return The row offset.
     */
    public int getRowOffset() {
        return rowOffset;
    }

    /**
     * Returns the column offset of this direction.
     *
     * @return The column offset.
     */
    public int getColOffset() {
        return colOffset;
    }
}
