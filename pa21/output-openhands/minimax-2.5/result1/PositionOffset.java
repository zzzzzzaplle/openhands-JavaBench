package game;

/**
 * Represents a position offset (delta row, delta col).
 */
public class PositionOffset {
    private int dRow;
    private int dCol;

    public PositionOffset(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    public int getRowOffset() {
        return dRow;
    }

    public int getColOffset() {
        return dCol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionOffset that = (PositionOffset) o;
        return dRow == that.dRow && dCol == that.dCol;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(dRow, dCol);
    }

    public PositionOffset clone() {
        return new PositionOffset(dRow, dCol);
    }
}