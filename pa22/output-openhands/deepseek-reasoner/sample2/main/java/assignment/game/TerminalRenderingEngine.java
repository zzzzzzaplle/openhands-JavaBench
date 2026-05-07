package assignment.game;

import java.io.PrintStream;

/**
 * Renders the Sokoban game state to a terminal output stream (e.g., System.out).
 * <p>
 * Character representations per cell:
 * <ul>
 *   <li>Wall: {@code #}</li>
 *   <li>Box: lowercase letter ({@code a}-{@code z}) based on playerId</li>
 *   <li>Player: uppercase letter ({@code A}-{@code Z}) based on playerId</li>
 *   <li>Empty cell at a destination: {@code @}</li>
 *   <li>Other empty cells: {@code .}</li>
 * </ul>
 */
public class TerminalRenderingEngine implements RenderingEngine {

    private final PrintStream outputStream;

    /**
     * Creates a TerminalRenderingEngine that writes to the given output stream.
     *
     * @param outputStream the output stream (typically System.out)
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
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < width; x++) {
                Position pos = Position.of(x, y);
                Entity entity = state.getEntity(pos);
                line.append(renderCell(entity, pos, gameMap));
            }
            outputStream.println(line);
        }
    }

    private char renderCell(Entity entity, Position pos, GameMap gameMap) {
        if (entity instanceof Wall) {
            return '#';
        }
        if (entity instanceof Box box) {
            return (char) ('a' + box.getPlayerId());
        }
        if (entity instanceof Player player) {
            return (char) ('A' + player.getId());
        }
        // Empty or no entity — check if this is a destination
        if (gameMap.getDestinations().contains(pos)) {
            return '@';
        }
        return '.';
    }

    @Override
    public void message(String content) {
        outputStream.println(content);
    }
}
