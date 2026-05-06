package game.map;

/**
 * Abstract base class for all cell types in the map.
 */
public abstract class Cell implements MapElement {

    protected final Coordinate coord;

    public Cell(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Parses a map character to create a concrete cell.
     *
     * @param c              The character to parse.
     * @param coord          The coordinate for the cell.
     * @param terminationType The termination type (for SOURCE/SINK).
     * @return The corresponding cell, or null if the character is invalid.
     */
    public static Cell fromChar(char c, Coordinate coord, TerminationType terminationType) {
        return switch (c) {
            case PipePatterns.WALL -> new Wall(coord);
            case '^', 'v', '<', '>' -> new TerminationCell(coord, charToDirection(c), terminationType);
            default -> {
                // For any other character, treat as fillable cell (can hold a pipe)
                yield new FillableCell(coord);
            }
        };
    }

    /**
     * Converts arrow characters to directions.
     */
    private static Direction charToDirection(char c) {
        return switch (c) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DOWN;
            case '<' -> Direction.LEFT;
            case '>' -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Unknown direction char: " + c);
        };
    }

    /**
     * Gets the coordinate of this cell.
     *
     * @return The coordinate.
     */
    public Coordinate getCoord() {
        return coord;
    }
}