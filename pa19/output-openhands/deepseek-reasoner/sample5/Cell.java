/**
 * Abstract base for all cell types on the map.
 */
public abstract class Cell implements MapElement {

    public final Coordinate coord;

    protected Cell(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Factory method that parses a character into a concrete Cell instance.
     *
     * @param c               Map character
     * @param coord           Coordinate of the cell
     * @param terminationType TerminationType to use if the char represents a termination cell
     * @return Appropriate Cell subclass
     */
    public static Cell fromChar(char c, Coordinate coord, TerminationType terminationType) {
        if (c == PipePatterns.WALL) {
            return new Wall(coord);
        }

        Direction dir = arrowToDirection(c);
        if (dir != null) {
            return new TerminationCell(coord, dir, terminationType);
        }

        // Default: fillable cell (empty)
        return new FillableCell(coord);
    }

    /**
     * Maps an arrow character to a Direction, or returns null if not an arrow.
     */
    private static Direction arrowToDirection(char c) {
        if (c == PipePatterns.Unfilled.UP_ARROW || c == PipePatterns.Filled.UP_ARROW) {
            return Direction.UP;
        }
        if (c == PipePatterns.Unfilled.DOWN_ARROW || c == PipePatterns.Filled.DOWN_ARROW) {
            return Direction.DOWN;
        }
        if (c == PipePatterns.Unfilled.LEFT_ARROW || c == PipePatterns.Filled.LEFT_ARROW) {
            return Direction.LEFT;
        }
        if (c == PipePatterns.Unfilled.RIGHT_ARROW || c == PipePatterns.Filled.RIGHT_ARROW) {
            return Direction.RIGHT;
        }
        return null;
    }
}
