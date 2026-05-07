import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializes map input files into runtime game objects.
 */
public class Deserializer {

    private static final Logger LOG = Logger.getLogger(Deserializer.class.getName());

    private final Path path;

    /**
     * Constructs a Deserializer from a file path string.
     */
    public Deserializer(String path) {
        this(Paths.get(path));
    }

    /**
     * Private constructor that validates file existence.
     */
    private Deserializer(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File not found: " + path);
        }
        this.path = path;
    }

    /**
     * Parses the file and creates a Game instance.
     *
     * @return the constructed Game, or null on failure
     */
    public Game parseGame() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {

            // Read rows, cols
            String line = getFirstNonEmptyLine(br);
            if (line == null) {
                LOG.severe("Unexpected EOF while reading dimensions");
                return null;
            }
            String[] dimensions = line.trim().split("\\s+");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

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
                    mapBuilder.append('\n');
                }
                mapBuilder.append(line);
            }

            // Read optional default pipes
            List<Pipe> pipes = new ArrayList<>();
            while ((line = getFirstNonEmptyLine(br)) != null) {
                String[] pipeCodes = line.trim().split("\\s+");
                for (String code : pipeCodes) {
                    try {
                        pipes.add(Pipe.fromCode(code));
                    } catch (IllegalArgumentException e) {
                        LOG.warning("Unknown pipe code: " + code);
                    }
                }
            }

            return Pa19FactoryHelper.createGame(rows, cols, delay, mapBuilder.toString(), pipes);

        } catch (FileNotFoundException e) {
            LOG.severe("File not found: " + path);
            return null;
        } catch (IOException e) {
            LOG.severe("I/O error reading file: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            LOG.severe("Invalid number format: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts multiline text to a Cell matrix.
     * Uses SINK type on border cells and SOURCE type on inner cells.
     */
    public static Cell[][] parseString(int rows, int cols, String cellsRep) {
        Cell[][] cells = new Cell[rows][cols];
        String[] lines = cellsRep.split("\n");

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

    private static boolean isBorderCell(int row, int col, int rows, int cols) {
        return row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
    }

    /**
     * Returns the first non-blank, non-comment line from the reader.
     * Comment lines start with '#'.
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
}
