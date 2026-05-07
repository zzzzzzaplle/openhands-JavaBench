import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes map input files into runtime game objects.
 */
public class Deserializer {

    private final Path path;

    /**
     * Creates a deserializer for the given file path string.
     *
     * @param path path to the input file
     * @throws FileNotFoundException if the file does not exist
     */
    public Deserializer(String path) throws FileNotFoundException {
        this(Paths.get(path));
    }

    private Deserializer(Path path) throws FileNotFoundException {
        if (!path.toFile().exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        this.path = path;
    }

    /**
     * Parses the file and creates a Game instance.
     *
     * @return the parsed Game, or null on failure
     */
    public Game parseGame() {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String firstLine = getFirstNonEmptyLine(br);
            if (firstLine == null) {
                return null;
            }
            String[] dimensions = firstLine.trim().split("\\s+");
            if (dimensions.length < 2) {
                return null;
            }
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            String delayLine = getFirstNonEmptyLine(br);
            if (delayLine == null) {
                return null;
            }
            int delay = Integer.parseInt(delayLine.trim());

            // Read map lines
            StringBuilder mapBuilder = new StringBuilder();
            for (int i = 0; i < rows; i++) {
                String line = getFirstNonEmptyLine(br);
                if (line == null) {
                    return null;
                }
                mapBuilder.append(line).append("\n");
            }

            // Read optional default pipes
            List<Pipe> pipes = new ArrayList<>();
            String pipesLine = getFirstNonEmptyLine(br);
            if (pipesLine != null) {
                String[] pipeCodes = pipesLine.trim().split("\\s+");
                for (String code : pipeCodes) {
                    Pipe pipe = Pipe.fromString(code);
                    if (pipe != null) {
                        pipes.add(pipe);
                    }
                }
            }

            Cell[][] cells = parseString(rows, cols, mapBuilder.toString());
            return Pa19FactoryHelper.createGame(rows, cols, delay, cells, pipes.isEmpty() ? null : pipes);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts a multiline string representation into a 2-D Cell array.
     * Border cells are treated as SINK candidates, inner cells as SOURCE candidates.
     *
     * @param rows    number of rows
     * @param cols    number of columns
     * @param cellsRep multiline string of cell characters
     * @return the constructed cell grid
     */
    public static Cell[][] parseString(int rows, int cols, String cellsRep) {
        Cell[][] cells = new Cell[rows][cols];
        String[] lines = cellsRep.split("\n");
        for (int r = 0; r < rows && r < lines.length; r++) {
            String line = lines[r];
            for (int c = 0; c < cols && c < line.length(); c++) {
                char ch = line.charAt(c);
                Coordinate coord = new Coordinate(r, c);
                boolean isBorder = r == 0 || r == rows - 1 || c == 0 || c == cols - 1;
                TerminationType type = isBorder ? TerminationType.SINK : TerminationType.SOURCE;
                cells[r][c] = Pa19FactoryHelper.cellFromChar(ch, coord, type);
            }
            // Fill remaining columns with FillableCell if line is shorter
            for (int c = line.length(); c < cols; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c));
            }
        }
        // Fill remaining rows with FillableCell
        for (int r = lines.length; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c));
            }
        }
        return cells;
    }

    /**
     * Returns the first non-empty, non-comment line from the reader.
     * Lines starting with '#' are treated as comments.
     *
     * @param br the buffered reader
     * @return the first non-blank, non-comment line, or null at end of stream
     * @throws IOException if an I/O error occurs
     */
    private String getFirstNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                return trimmed;
            }
        }
        return null;
    }
}
