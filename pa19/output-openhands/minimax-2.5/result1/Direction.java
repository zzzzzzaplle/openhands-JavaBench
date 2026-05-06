package game.map;

/**
 * Enumeration of directions for pipe connections and movement.
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    /**
     * Returns the opposite direction.
     *
     * @return The opposite direction.
     */
    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    /**
     * Returns the unit coordinate offset for movement in this direction.
     *
     * @return A new Coordinate representing the offset.
     */
    public Coordinate getOffset() {
        return switch (this) {
            case UP -> new Coordinate(-1, 0);
            case DOWN -> new Coordinate(1, 0);
            case LEFT -> new Coordinate(0, -1);
            case RIGHT -> new Coordinate(0, 1);
        };
    }
}