import java.io.PrintStream;
import java.util.Map;

/**
 * Renders the game board and messages to a terminal output stream.
 */
public class TerminalRenderingEngine implements RenderingEngine {

    private final PrintStream outputStream;

    /**
     * Create a terminal rendering engine writing to the given output stream.
     *
     * @param outputStream the output stream (typically System.out)
     */
    public TerminalRenderingEngine(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void render(GameState state) {
        GameMap gameMap = state.getGameMap();
        Map<Position, Entity> entities = state.getEntities();

        for (int y = 0; y < gameMap.getMaxHeight(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < gameMap.getMaxWidth(); x++) {
                Position pos = Position.of(x, y);
                Entity entity = entities.get(pos);

                char displayChar;
                if (entity instanceof Wall) {
                    displayChar = '#';
                } else if (entity instanceof Box box) {
                    displayChar = (char) ('a' + box.getPlayerId());
                } else if (entity instanceof Player player) {
                    displayChar = (char) ('A' + player.getId());
                } else if (entity instanceof Empty) {
                    if (gameMap.getDestinations().contains(pos)) {
                        displayChar = '@';
                    } else {
                        displayChar = '.';
                    }
                } else {
                    // Unknown or null — render as space
                    displayChar = ' ';
                }
                row.append(displayChar);
            }
            outputStream.println(row);
        }
    }

    @Override
    public void message(String content) {
        outputStream.println(content);
    }
}
