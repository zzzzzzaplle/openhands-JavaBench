package game.util;

import game.Pa19FactoryHelper;
import game.TerminationType;
import game.map.Cell;
import game.map.Coordinate;
import game.Game;
import game.pipe.Pipe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deserializes map input into runtime objects.
 */
public class Deserializer {

    private static final Logger LOG = Logger.getLogger(Deserializer.class.getName());

    private final Path path;

    /**
     * Constructs a Deserializer from a string path.
     *
     * @param path file path
     * @throws FileNotFoundException if file does not exist
     */
    public Deserializer(String path) throws FileNotFoundException {
        this(Path.of(path));
    }

    /**
     * Constructs a Deserializer from a Path.
     *
     * @param path file path
     * @throws FileNotFoundException if file does not exist
     */
    private Deserializer(Path path) throws FileNotFoundException {
        if (!path.toFile().exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        this.path = path;
    }

    /**
     * Parses a game from the input file.
     *
     * @return parsed Game, or null on failure
     */
    public Game parseGame() {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            // Read rows and cols
            String line = getFirstNonEmptyLine(br);
            if (line == null) {
                LOG.severe("Unexpected EOF while reading rows/cols");
                return null;
            }
            String[] parts = line.trim().split("\\s+");
            int rows = Integer.parseInt(parts[0]);
            int cols = Integer.parseInt(parts[1]);

            // Read delay
            line = getFirstNonEmptyLine(br);
            if (line == null) {
                LOG.severe("Unexpected EOF while reading delay");
                return null;
            }
            int delay = Integer.parseInt(line.trim());

            // Read map lines
            StringBuilder mapBuilder = new StringBuilder();
            for (int i = 0; i < rows; i++) {
                line = getFirstNonEmptyLine(br);
                if (line == null) {
                    LOG.severe("Unexpected EOF while reading map lines");
                    return null;
                }
                if (mapBuilder.length() > 0) {
                    mapBuilder.append(System.lineSeparator());
                }
                mapBuilder.append(line);
            }

            // Read optional default pipes
            List<Pipe> pipes = new ArrayList<>();
            line = getFirstNonEmptyLine(br);
            if (line != null) {
                String[] pipeCodes = line.trim().split("\\s+");
                for (String code : pipeCodes) {
                    pipes.add(new Pipe(code));
                }
            }

            Cell[][] cells = parseString(rows, cols, mapBuilder.toString());
            return Pa19FactoryHelper.createGame(rows, cols, delay, cells, pipes);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "I/O error reading file", e);
            return null;
        }
    }

    /**
     * Converts a multiline string representation to a Cell matrix.
     * Uses SINK type for border termination cells and SOURCE type for inner termination cells.
     *
     * @param rows     number of rows
     * @param cols     number of columns
     * @param cellsRep multiline string of cell characters
     * @return 2D Cell array
     */
    public static Cell[][] parseString(int rows, int cols, String cellsRep) {
        Cell[][] cells = new Cell[rows][cols];
        String[] lines = cellsRep.split("\\r?\\n");

        for (int r = 0; r < rows && r < lines.length; r++) {
            String line = lines[r];
            for (int c = 0; c < cols && c < line.length(); c++) {
                char ch = line.charAt(c);
                Coordinate coord = new Coordinate(r, c);
                TerminationType type = isBorderCell(r, c, rows, cols)
                        ? TerminationType.SINK
                        : TerminationType.SOURCE;
                cells[r][c] = Pa19FactoryHelper.cellFromChar(ch, coord, type);
            }
        }
        return cells;
    }

    /**
     * Returns the first non-blank, non-comment line from the reader.
     * Lines starting with '#' are treated as comments and skipped.
     *
     * @param br buffered reader
     * @return the first non-empty, non-comment line, or null at end of stream
     * @throws IOException if reading fails
     */
    private String getFirstNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                return line;
            }
        }
        return null;
    }

    /**
     * Determines if a cell position is on the border of the grid.
     */
    private static boolean isBorderCell(int row, int col, int rows, int cols) {
        return row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
    }
}
