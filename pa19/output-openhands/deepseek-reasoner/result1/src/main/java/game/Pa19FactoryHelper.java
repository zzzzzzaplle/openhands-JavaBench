package game;

import game.map.Cell;
import game.map.Coordinate;
import game.pipe.Pipe;

import java.util.List;

/**
 * Factory helper for creating game objects from parsed data.
 */
public final class Pa19FactoryHelper {

    private Pa19FactoryHelper() {
        // prevent instantiation
    }

    /**
     * Creates a cell from a character, coordinate, and termination type.
     *
     * @param c               character
     * @param coord           coordinate
     * @param terminationType SOURCE or SINK
     * @return created Cell
     */
    public static Cell cellFromChar(char c, Coordinate coord, TerminationType terminationType) {
        return Cell.fromChar(c, coord, terminationType);
    }

    /**
     * Creates a Game instance from parsed parameters.
     *
     * @param rows  number of rows
     * @param cols  number of columns
     * @param delay delay value
     * @param cells cell array
     * @param pipes initial pipe list
     * @return created Game
     */
    public static Game createGame(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        return new Game(rows, cols, delay, cells, pipes);
    }
}
