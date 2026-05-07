import java.util.Objects;

/**
 * Represents an immutable 2D grid coordinate on the game board.
 */
public class Position {

    private final int x;
    private final int y;

    /**
     * Creates a new Position.
     *
     * @param x the x-coordinate (column)
     * @param y the y-coordinate (row)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate.
     *
     * @return x-coordinate
     */
    public int x() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     *
     * @return y-coordinate
     */
    public int y() {
        return y;
    }

    /**
     * Factory method for concise position creation.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return a new Position
     */
    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
