import java.io.PrintStream;
import java.util.Set;

/**
 * Renders the game board to a terminal output stream using character
 * representations: # for walls, A-Z for players, a-z for boxes,
 * @ for empty destinations, and . for empty spaces.
 */
public class TerminalRenderingEngine implements RenderingEngine {

    private final PrintStream outputStream;

    /**
     * Create a new terminal rendering engine.
     *
     * @param outputStream the output stream (typically System.out)
     */
    public TerminalRenderingEngine(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void render(GameState state) {
        GameMap gameMap = state.getGameMap();
        int maxWidth = gameMap.getMaxWidth();
        int maxHeight = gameMap.getMaxHeight();
        Set<Position> destinations = gameMap.getDestinations();

        for (int y = 0; y < maxHeight; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < maxWidth; x++) {
                Position pos = Position.of(x, y);
                line.append(renderCell(state, gameMap, destinations, pos));
            }
            outputStream.println(line);
        }
    }

    /**
     * Render a single cell of the game board.
     */
    private char renderCell(GameState state, GameMap gameMap, Set<Position> destinations, Position pos) {
        Entity entity = state.getEntity(pos);

        if (entity instanceof Wall) {
            return '#';
        }
        if (entity instanceof Player) {
            return (char) ('A' + ((Player) entity).getId());
        }
        if (entity instanceof Box) {
            return (char) ('a' + ((Box) entity).getPlayerId());
        }
        if (destinations.contains(pos)) {
            return '@';
        }
        return '.';
    }

    @Override
    public void message(String content) {
        outputStream.println(content);
    }
}
