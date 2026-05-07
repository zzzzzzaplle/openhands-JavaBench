import java.util.List;

/**
 * Factory helper for creating game objects, used by the Deserializer.
 */
public final class Pa19FactoryHelper {

    private Pa19FactoryHelper() {
        // non-instantiable
    }

    /**
     * Creates a Game instance from parsed data.
     */
    public static Game createGame(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Creates a Cell from a character, coordinate, and termination type.
     */
    public static Cell cellFromChar(char ch, Coordinate coord, TerminationType terminationType) {
        return Cell.fromChar(ch, coord, terminationType);
    }
}
