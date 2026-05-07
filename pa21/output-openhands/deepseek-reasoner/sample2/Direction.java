/**
 * Enum representing the four cardinal movement directions on a 2D grid.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final PositionOffset offset;

    /**
     * Constructs a direction with the given row and column offsets.
     *
     * @param dRow Row offset.
     * @param dCol Column offset.
     */
    Direction(final int dRow, final int dCol) {
        this.offset = new PositionOffset(dRow, dCol);
    }

    /**
     * Returns the {@link PositionOffset} associated with this direction.
     *
     * @return The position offset for this direction.
     */
    public PositionOffset getOffset() {
        return offset;
    }

    /**
     * Returns the row offset for this direction.
     *
     * @return The row offset.
     */
    public int getRowOffset() {
        return offset.getDRow();
    }

    /**
     * Returns the column offset for this direction.
     *
     * @return The column offset.
     */
    public int getColOffset() {
        return offset.getDCol();
    }
}
