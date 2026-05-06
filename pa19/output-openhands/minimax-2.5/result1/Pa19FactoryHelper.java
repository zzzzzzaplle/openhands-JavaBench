package game.map;

/**
 * Helper class for factory methods in PA19 game.
 */
public final class Pa19FactoryHelper {

    private Pa19FactoryHelper() {
        // Prevent instantiation
    }

    /**
     * Creates a game from parsed components.
     *
     * @param rows    Number of rows.
     * @param cols   Number of columns.
     * @param delay  Delay value.
     * @param cells  The cell matrix.
     * @param pipes  Default pipes (can be empty list).
     * @return The created game.
     */
    public static Game createGame(int rows, int cols, int delay, Cell[][] cells, java.util.List<Pipe> pipes) {
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Creates a cell from a character.
     *
     * @param c              The character.
     * @param coord         The coordinate.
     * @param terminationType The termination type (if applicable).
     * @return The created cell.
     */
    public static Cell cellFromChar(char c, Coordinate coord, TerminationType terminationType) {
        return Cell.fromChar(c, coord, terminationType);
    }
}