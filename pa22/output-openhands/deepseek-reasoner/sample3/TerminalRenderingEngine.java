import java.io.PrintStream;

/**
 * Renders the game board and messages to a terminal output stream.
 * <p>
 * Character mapping:
 * Wall = '#', Box = lowercase letter (a-z based on playerId),
 * Player = uppercase letter (A-Z based on playerId),
 * Empty at destination = '@', other empty = '.'.
 * </p>
 */
public class TerminalRenderingEngine implements RenderingEngine {

    private final PrintStream outputStream;

    /**
     * Creates a TerminalRenderingEngine writing to the given print stream.
     *
     * @param outputStream the output stream (e.g., System.out)
     */
    public TerminalRenderingEngine(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void render(GameState state) {
        final GameMap map = state.getGameMap();
        for (int y = 0; y < map.getMaxHeight(); y++) {
            final StringBuilder row = new StringBuilder();
            for (int x = 0; x < map.getMaxWidth(); x++) {
                final Position pos = Position.of(x, y);
                final Entity entity = map.getEntity(pos);
                row.append(renderEntity(entity, pos, map));
            }
            outputStream.println(row.toString());
        }
    }

    private char renderEntity(Entity entity, Position pos, GameMap map) {
        if (entity instanceof Wall) {
            return '#';
        }
        if (entity instanceof Player) {
            final int id = ((Player) entity).getId();
            return (char) ('A' + id);
        }
        if (entity instanceof Box) {
            final int playerId = ((Box) entity).getPlayerId();
            return (char) ('a' + playerId);
        }
        if (entity instanceof Empty) {
            if (map.getDestinations().contains(pos)) {
                return '@';
            }
            return '.';
        }
        return ' ';
    }

    @Override
    public void message(String content) {
        outputStream.println(content);
    }
}
