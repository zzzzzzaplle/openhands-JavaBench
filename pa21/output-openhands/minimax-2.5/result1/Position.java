package game;

/**
 * Represents a position (row, col) on the game board.
 */
public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Offset this position by the given delta.
     */
    public Position offsetBy(int dRow, int dCol) {
        return new Position(row + dRow, col + dCol);
    }

    /**
     * Offset this position by the given offset.
     */
    public Position offsetBy(PositionOffset offset) {
        return offsetBy(offset.getRowOffset(), offset.getColOffset());
    }

    /**
     * Offset this position by the given delta, or return null if out of bounds.
     */
    public Position offsetByOrNull(int dRow, int dCol, int numRows, int numCols) {
        int newRow = row + dRow;
        int newCol = col + dCol;
        if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {
            return null;
        }
        return new Position(newRow, newCol);
    }

    /**
     * Offset this position by the given offset, or return null if out of bounds.
     */
    public Position offsetByOrNull(PositionOffset offset, int numRows, int numCols) {
        return offsetByOrNull(offset.getRowOffset(), offset.getColOffset(), numRows, numCols);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    public Position clone() {
        return new Position(row, col);
    }
}