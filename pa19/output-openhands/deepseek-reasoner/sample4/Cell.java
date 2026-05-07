/**
 * Abstract base for every cell on the map.
 */
public abstract class Cell implements MapElement {

    public final Coordinate coord;

    protected Cell(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Creates a concrete Cell from a map character.
     *
     * @param c                map character
     * @param coord            coordinate of the cell
     * @param terminationType  SOURCE or SINK (only relevant for arrow characters)
     * @return a new Cell instance
     */
    public static Cell fromChar(char c, Coordinate coord, TerminationType terminationType) {
        switch (c) {
            case '#':
            case '\u2593': // ▓
                return new Wall(coord);
            case '.':
                return new FillableCell(coord);
            case '\u25B2': // ▲  UP arrow (filled)
            case '\u25B3': // △  UP arrow (unfilled)
            case '^':
                return new TerminationCell(coord, Direction.UP, terminationType);
            case '\u25BC': // ▼  DOWN arrow (filled)
            case '\u25BD': // ▽  DOWN arrow (unfilled)
            case 'v':
                return new TerminationCell(coord, Direction.DOWN, terminationType);
            case '\u25C0': // ◀  LEFT arrow (filled)
            case '\u25C1': // ◁  LEFT arrow (unfilled)
            case '<':
                return new TerminationCell(coord, Direction.LEFT, terminationType);
            case '\u25B6': // ▶  RIGHT arrow (filled)
            case '\u25B7': // ▷  RIGHT arrow (unfilled)
            case '>':
                return new TerminationCell(coord, Direction.RIGHT, terminationType);
            default:
                return new FillableCell(coord);
        }
    }
}
