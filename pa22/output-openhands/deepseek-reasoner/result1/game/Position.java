package assignment.game;

import java.util.Objects;

/**
 * Represents a 2D coordinate on the game grid.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Create a new position.
     *
     * @param x the x-coordinate (column)
     * @param y the y-coordinate (row)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x-coordinate
     */
    public int x() {
        return x;
    }

    /**
     * @return the y-coordinate
     */
    public int y() {
        return y;
    }

    /**
     * Static factory method.
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
        Position position = (Position) o;
        return x == position.x && y == position.y;
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
