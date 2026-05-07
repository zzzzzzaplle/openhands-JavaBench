/**
 * Enumeration of the four cardinal movement directions on the game board.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final PositionOffset offset;

    Direction(final int dRow, final int dCol) {
        this.offset = new PositionOffset(dRow, dCol);
    }

    /**
     * Returns the position offset associated with this direction.
     *
     * @return The position offset.
     */
    public PositionOffset getOffset() {
        return offset;
    }

    /**
     * Returns the row delta for this direction.
     *
     * @return The row offset.
     */
    public int getRowOffset() {
        return offset.getDRow();
    }

    /**
     * Returns the column delta for this direction.
     *
     * @return The column offset.
     */
    public int getColOffset() {
        return offset.getDCol();
    }
}
