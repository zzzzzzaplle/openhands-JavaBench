/**
 * Abstract base class for all cell types on the map.
 */
public abstract class Cell implements MapElement {

    public final Coordinate coord;

    protected Cell(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Parses a character into a concrete Cell instance.
     *
     * @param c               the map character
     * @param coord           the coordinate of this cell
     * @param terminationType the termination type to use if this turns out to be a termination cell
     * @return the constructed Cell
     */
    public static Cell fromChar(char c, Coordinate coord, TerminationType terminationType) {
        if (c == PipePatterns.WALL) {
            return new Wall(coord);
        }
        Direction dir = directionFromChar(c);
        if (dir != null) {
            return new TerminationCell(coord, dir, terminationType);
        }
        return new FillableCell(coord);
    }

    private static Direction directionFromChar(char c) {
        switch (c) {
            case '^':
            case '\u25B2':
            case '\u25B3':
                return Direction.UP;
            case 'v':
            case '\u25BC':
            case '\u25BD':
                return Direction.DOWN;
            case '<':
            case '\u25C0':
            case '\u25C1':
                return Direction.LEFT;
            case '>':
            case '\u25B6':
            case '\u25B7':
                return Direction.RIGHT;
            default:
                return null;
        }
    }
}
