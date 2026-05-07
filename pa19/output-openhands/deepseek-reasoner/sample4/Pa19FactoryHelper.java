import java.util.List;

/**
 * Factory helper for creating game objects from parsed data.
 */
public final class Pa19FactoryHelper {

    private Pa19FactoryHelper() {
    }

    /**
     * Creates a Cell from a map character, using the border/inner position to
     * determine termination type.
     *
     * @param c       map character
     * @param coord   coordinate of the cell
     * @param type    SOURCE for inner cells, SINK for border cells (if arrow char)
     * @return a new Cell instance
     */
    public static Cell cellFromChar(char c, Coordinate coord, TerminationType type) {
        return Cell.fromChar(c, coord, type);
    }

    /**
     * Creates a Game instance with the given parameters.
     *
     * @param rows  number of rows
     * @param cols  number of columns
     * @param delay initial delay
     * @param cells cell grid
     * @param pipes initial pipes (may be null)
     * @return a new Game instance
     */
    public static Game createGame(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        return new Game(rows, cols, delay, cells, pipes);
    }
}
