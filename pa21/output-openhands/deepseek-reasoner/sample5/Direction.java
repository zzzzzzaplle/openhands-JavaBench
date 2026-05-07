/**
 * Enumeration of the four cardinal directions for movement on the game board.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final PositionOffset offset;

    /**
     * Creates a direction with the specified row and column offsets.
     *
     * @param dRow The row offset.
     * @param dCol The column offset.
     */
    Direction(final int dRow, final int dCol) {
        this.offset = new PositionOffset(dRow, dCol);
    }

    /**
     * @return The position offset associated with this direction.
     */
    public PositionOffset getOffset() {
        return offset;
    }

    /**
     * @return The row offset of this direction.
     */
    public int getRowOffset() {
        return offset.getDRow();
    }

    /**
     * @return The column offset of this direction.
     */
    public int getColOffset() {
        return offset.getDCol();
    }
}
