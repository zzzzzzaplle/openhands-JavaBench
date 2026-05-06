package game;

/**
 * Direction constants for movement.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final PositionOffset offset;

    Direction(int rowOffset, int colOffset) {
        this.offset = new PositionOffset(rowOffset, colOffset);
    }

    public PositionOffset getOffset() {
        return offset;
    }

    public int getRowOffset() {
        return offset.getRowOffset();
    }

    public int getColOffset() {
        return offset.getColOffset();
    }
}