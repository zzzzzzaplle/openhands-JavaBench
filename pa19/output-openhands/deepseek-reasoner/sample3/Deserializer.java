import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes map input into runtime objects.
 */
public class Deserializer {
    private final Path path;

    public Deserializer(String path) {
        this(Path.of(path));
    }

    private Deserializer(Path path) {
        if (!Files.exists(path)) {
            try {
                throw new FileNotFoundException("File not found: " + path);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        this.path = path;
    }

    /**
     * Parses a complete game from the input file.
     */
    public Game parseGame() {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            // Read rows and cols
            String line = getFirstNonEmptyLine(br);
            if (line == null) {
                return null;
            }
            String[] parts = line.trim().split("\\s+");
            int rows = Integer.parseInt(parts[0]);
            int cols = Integer.parseInt(parts[1]);

            // Read delay
            line = getFirstNonEmptyLine(br);
            if (line == null) {
                return null;
            }
            int delay = Integer.parseInt(line.trim());

            // Read map lines
            StringBuilder cellsRepBuilder = new StringBuilder();
            for (int i = 0; i < rows; i++) {
                line = getFirstNonEmptyLine(br);
                if (line == null) {
                    return null;
                }
                if (cellsRepBuilder.length() > 0) {
                    cellsRepBuilder.append(System.lineSeparator());
                }
                cellsRepBuilder.append(line);
            }
            String cellsRep = cellsRepBuilder.toString();

            // Read optional default pipes
            List<Pipe> pipes = new ArrayList<>();
            String pipesLine = getFirstNonEmptyLine(br);
            if (pipesLine != null) {
                String[] pipeCodes = pipesLine.trim().split("\\s+");
                for (String code : pipeCodes) {
                    pipes.add(Pipe.fromCode(code));
                }
            }

            return Pa19FactoryHelper.createGame(rows, cols, delay,
                    Deserializer.parseString(rows, cols, cellsRep), pipes);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts a multiline string to a Cell matrix.
     */
    public static Cell[][] parseString(int rows, int cols, String cellsRep) {
        Cell[][] cells = new Cell[rows][cols];
        String[] lines = cellsRep.split("\\r?\\n");

        for (int r = 0; r < rows && r < lines.length; r++) {
            String line = lines[r];
            for (int c = 0; c < cols && c < line.length(); c++) {
                char ch = line.charAt(c);
                Coordinate coord = new Coordinate(r, c);

                // Determine termination type based on position
                boolean onEdge = r == 0 || r == rows - 1 || c == 0 || c == cols - 1;
                TerminationType type = onEdge ? TerminationType.SINK : TerminationType.SOURCE;

                cells[r][c] = Pa19FactoryHelper.cellFromChar(ch, coord, type);
            }
        }
        return cells;
    }

    /**
     * Returns the first non-blank, non-comment line from the reader.
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
