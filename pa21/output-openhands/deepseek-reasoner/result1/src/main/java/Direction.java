/**
 * Enum representing the four cardinal directions for movement.
 */
public enum Direction {

    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final PositionOffset offset;

    /**
     * Creates a direction with the given row and column offsets.
     *
     * @param dRow Row offset.
     * @param dCol Column offset.
     */
    Direction(final int dRow, final int dCol) {
        this.offset = new PositionOffset(dRow, dCol);
    }

    /**
     * Returns the position offset for this direction.
     *
     * @return The position offset.
     */
    public PositionOffset getOffset() {
        return offset;
    }

    /**
     * Returns the row offset for this direction.
     *
     * @return Row offset.
     */
    public int getRowOffset() {
        return offset.getDRow();
    }

    /**
     * Returns the column offset for this direction.
     *
     * @return Column offset.
     */
    public int getColOffset() {
        return offset.getDCol();
    }
}
