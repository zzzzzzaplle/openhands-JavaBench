import java.util.List;

/**
 * Factory helper for creating Game and Cell instances.
 */
public final class Pa19FactoryHelper {
    private Pa19FactoryHelper() {
    }

    /**
     * Creates a Game from parsed data.
     */
    public static Game createGame(int rows, int cols, int delay,
                                  Cell[][] cells, List<Pipe> pipes) {
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Creates a Cell from a character, coordinate, and termination type.
     */
    public static Cell cellFromChar(char c, Coordinate coord, TerminationType terminationType) {
        return Cell.fromChar(c, coord, terminationType);
    }
}
