/**
 * Abstract base for all map cells.
 */
public abstract class Cell implements MapElement {
    public final Coordinate coord;

    protected Cell(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Parses a character into a concrete Cell instance.
     */
    public static Cell fromChar(char c, Coordinate coord, TerminationType terminationType) {
        if (c == PipePatterns.WALL || c == '#') {
            return new Wall(coord);
        }
        if (c == PipePatterns.Filled.UP_ARROW || c == PipePatterns.Unfilled.UP_ARROW
                || c == '^') {
            return new TerminationCell(coord, Direction.UP, terminationType);
        }
        if (c == PipePatterns.Filled.DOWN_ARROW || c == PipePatterns.Unfilled.DOWN_ARROW
                || c == 'v') {
            return new TerminationCell(coord, Direction.DOWN, terminationType);
        }
        if (c == PipePatterns.Filled.LEFT_ARROW || c == PipePatterns.Unfilled.LEFT_ARROW
                || c == '<') {
            return new TerminationCell(coord, Direction.LEFT, terminationType);
        }
        if (c == PipePatterns.Filled.RIGHT_ARROW || c == PipePatterns.Unfilled.RIGHT_ARROW
                || c == '>') {
            return new TerminationCell(coord, Direction.RIGHT, terminationType);
        }
        // Default: fillable cell (empty)
        return new FillableCell(coord);
    }
}
