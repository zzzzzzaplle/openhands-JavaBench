import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes map input files into runtime objects.
 */
public class Deserializer {

    private final Path path;

    /**
     * Constructs a Deserializer from a file path string.
     *
     * @param path File path
     * @throws FileNotFoundException if the file does not exist
     */
    public Deserializer(String path) throws FileNotFoundException {
        this(Paths.get(path));
    }

    /**
     * Private constructor that validates file existence.
     *
     * @param path File path
     * @throws FileNotFoundException if the file does not exist
     */
    private Deserializer(Path path) throws FileNotFoundException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + path);
        }
        this.path = path;
    }

    /**
     * Parses a complete game from the input file.
     * Format: first line: rows cols delay, subsequent lines: map rows,
     * optionally followed by default pipe shapes.
     *
     * @return Game instance, or null on error
     */
    public Game parseGame() {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            // Read dimensions and delay
            String header = getFirstNonEmptyLine(br);
            if (header == null) {
                return null;
            }
            String[] parts = header.trim().split("\\s+");
            int rows = Integer.parseInt(parts[0]);
            int cols = Integer.parseInt(parts[1]);
            int delay = Integer.parseInt(parts[2]);

            // Read map lines
            StringBuilder mapBuilder = new StringBuilder();
            for (int i = 0; i < rows; i++) {
                String line = getFirstNonEmptyLine(br);
                if (line == null) {
                    return null; // Unexpected EOF
                }
                if (mapBuilder.length() > 0) {
                    mapBuilder.append("\n");
                }
                mapBuilder.append(line.trim());
            }

            // Read optional default pipes
            List<Pipe> pipes = new ArrayList<>();
            String pipeLine = getFirstNonEmptyLine(br);
            if (pipeLine != null) {
                String[] pipeCodes = pipeLine.trim().split("\\s+");
                for (String code : pipeCodes) {
                    Pipe pipe = parsePipe(code);
                    if (pipe != null) {
                        pipes.add(pipe);
                    }
                }
            }

            return Game.fromString(rows, cols, delay, mapBuilder.toString(),
                    pipes.isEmpty() ? null : pipes);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses a string representation into a Cell matrix.
     *
     * @param rows     Number of rows
     * @param cols     Number of columns
     * @param cellsRep Multiline string of cell characters
     * @return 2D Cell array
     */
    static Cell[][] parseString(int rows, int cols, String cellsRep) {
        Cell[][] cells = new Cell[rows][cols];
        String[] lines = cellsRep.split("\n");

        for (int r = 0; r < rows; r++) {
            String line = (r < lines.length) ? lines[r] : "";
            for (int c = 0; c < cols; c++) {
                char ch = (c < line.length()) ? line.charAt(c) : '.';
                Coordinate coord = new Coordinate(r, c);

                // Border cells default to SINK, inner cells default to SOURCE
                TerminationType type = isBorder(r, c, rows, cols)
                        ? TerminationType.SINK
                        : TerminationType.SOURCE;

                cells[r][c] = Cell.fromChar(ch, coord, type);
            }
        }

        return cells;
    }

    /**
     * Checks if a position is on the map border.
     */
    private static boolean isBorder(int row, int col, int rows, int cols) {
        return row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
    }

    /**
     * Reads the first non-empty, non-comment line from the reader.
     *
     * @param br BufferedReader
     * @return The line content, or null at end of stream
     * @throws IOException on I/O error
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

    /**
     * Parses a two-character pipe code into a Pipe.
     *
     * @param code Two-character code (e.g., "HZ", "VT", "TL", "TR", "BL", "BR", "CR")
     * @return Pipe instance, or null if unknown code
     */
    private static Pipe parsePipe(String code) {
        switch (code.toUpperCase()) {
            case "HZ": return new Pipe(PipeShape.HORIZONTAL);
            case "VT": return new Pipe(PipeShape.VERTICAL);
            case "TL": return new Pipe(PipeShape.TOP_LEFT);
            case "TR": return new Pipe(PipeShape.TOP_RIGHT);
            case "BL": return new Pipe(PipeShape.BOTTOM_LEFT);
            case "BR": return new Pipe(PipeShape.BOTTOM_RIGHT);
            case "CR": return new Pipe(PipeShape.CROSS);
            default:
                System.err.println("Unknown pipe code: " + code);
                return null;
        }
    }
}
