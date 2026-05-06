package assignment.game;

import java.io.PrintStream;

/**
 * Terminal-based rendering engine that displays the game board and messages
 * to a PrintStream.
 */
public class TerminalRenderingEngine implements RenderingEngine {
    private final PrintStream outputStream;

    /**
     * Create a terminal rendering engine.
     *
     * @param outputStream the output stream (e.g., System.out)
     */
    public TerminalRenderingEngine(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void render(GameState state) {
        GameMap gameMap = state.getGameMap();
        int width = gameMap.getMaxWidth();
        int height = gameMap.getMaxHeight();

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {
                Position pos = Position.of(x, y);
                Entity entity = state.getEntity(pos);

                if (entity instanceof Wall) {
                    sb.append('#');
                } else if (entity instanceof Player) {
                    char c = (char) ('A' + ((Player) entity).getId());
                    sb.append(c);
                } else if (entity instanceof Box) {
                    char c = (char) ('a' + ((Box) entity).getPlayerId());
                    sb.append(c);
                } else if (entity instanceof Empty) {
                    if (gameMap.getDestinations().contains(pos)) {
                        sb.append('@');
                    } else {
                        sb.append('.');
                    }
                }
            }
            outputStream.println(sb.toString());
        }
    }

    @Override
    public void message(String content) {
        outputStream.println(content);
    }
}
