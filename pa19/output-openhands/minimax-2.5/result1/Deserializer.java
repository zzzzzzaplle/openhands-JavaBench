package game.map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes map input into runtime objects.
 */
public class Deserializer {

    private final Path path;

    /**
     * Constructs a deserializer from a file path string.
     *
     * @param path The file path string.
     */
    public Deserializer(String path) {
        this(Path.of(path));
    }

    /**
     * Constructs a deserializer from a Path.
     *
     * @param path The file path.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    private Deserializer(Path path) throws FileNotFoundException {
        if (!path.toFile().exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        this.path = path;
    }

    /**
     * Parses the game from the file.
     *
     * @return The parsed game, or null on error.
     */
    public Game parseGame() {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line = getFirstNonEmptyLine(br);
            if (line == null) {
                return null;
            }

            String[] parts = line.trim().split("\\s+");
            int rows = Integer.parseInt(parts[0]);
            int cols = Integer.parseInt(parts[1]);
            int delay = Integer.parseInt(parts[2]);

            // Read map lines
            List<String> mapLines = new ArrayList<>();
            for (int i = 0; i < rows; i++) {
                String mapLine = getFirstNonEmptyLine(br);
                if (mapLine == null) {
                    System.err.println("Unexpected EOF while reading map");
                    return null;
                }
                // Trim or pad to cols length
                if (mapLine.length() < cols) {
                    mapLine = StringUtils.createPadding(cols - mapLine.length(), ' ').toString() + mapLine;
                } else {
                    mapLine = mapLine.substring(0, cols);
                }
                mapLines.add(mapLine);
            }

            // Build cells representation
            StringBuilder cellsRep = new StringBuilder();
            for (String mapLine : mapLines) {
                cellsRep.append(mapLine).append("\n");
            }

            // Try to read optional pipes
            List<Pipe> pipes = new ArrayList<>();
            String pipeLine;
            while ((pipeLine = getFirstNonEmptyLine(br)) != null) {
                if (pipeLine.startsWith("#")) {
                    continue;
                }
                String[] pipeCodes = pipeLine.trim().split("\\s+");
                for (String code : pipeCodes) {
                    if (!code.isEmpty()) {
                        pipes.add(new Pipe(code));
                    }
                }
            }

            return Pa19FactoryHelper.createGame(rows, cols, delay, null, pipes);

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses a string representation to a cell matrix.
     *
     * @param rows     Number of rows.
     * @param cols    Number of columns.
     * @param cellsRep The string representation.
     * @return The cell matrix.
     */
    public static Cell[][] parseString(int rows, int cols, String cellsRep) {
        Cell[][] cells = new Cell[rows][cols];
        String[] lines = cellsRep.split("\n");

        for (int i = 0; i < rows; i++) {
            String line = i < lines.length ? lines[i] : "";
            for (int j = 0; j < cols; j++) {
                char c = j < line.length() ? line.charAt(j) : '.';
                Coordinate coord = new Coordinate(i, j);

                // Determine termination type based on position
                TerminationType terminationType = null;

                // Check if it's a termination cell marker
                if (c == '^' || c == 'v' || c == '<' || c == '>') {
                    // Determine if SOURCE (non-edge) or SINK (edge)
                    if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                        terminationType = TerminationType.SINK;
                    } else {
                        terminationType = TerminationType.SOURCE;
                    }
                }

                cells[i][j] = Pa19FactoryHelper.cellFromChar(c, coord, terminationType);
            }
        }

        return cells;
    }

    /**
     * Returns the first non-blank, non-comment line.
     *
     * @param br The BufferedReader.
     * @return The line, or null at end of stream.
     * @throws IOException On I/O error.
     */
    private String getFirstNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                return line;
            }
        }
        return null;
    }
}