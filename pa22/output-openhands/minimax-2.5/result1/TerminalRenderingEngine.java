package game;

import java.io.PrintStream;

/**
 * Terminal rendering engine for displaying the game board.
 */
public class TerminalRenderingEngine implements RenderingEngine {
    private PrintStream outputStream;

    public TerminalRenderingEngine(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void render(GameState state) {
        GameMap map = state.getGameMap();
        int maxHeight = map.getMaxHeight();
        int maxWidth = map.getMaxWidth();

        for (int y = 0; y < maxHeight; y++) {
            for (int x = 0; x < maxWidth; x++) {
                Position pos = new Position(x, y);
                Entity entity = map.getEntity(pos);

                if (entity == null) {
                    outputStream.print('.');
                } else if (entity instanceof Wall) {
                    outputStream.print('#');
                } else if (entity instanceof Player) {
                    outputStream.print(((Player) entity).toChar());
                } else if (entity instanceof Box) {
                    outputStream.print(((Box) entity).toChar());
                } else if (entity instanceof Empty) {
                    // Check if this is a destination
                    if (map.getDestinations().contains(pos)) {
                        outputStream.print('@');
                    } else {
                        outputStream.print('.');
                    }
                } else {
                    outputStream.print('.');
                }
            }
            outputStream.println();
        }
    }

    @Override
    public void message(String content) {
        outputStream.println(content);
    }
}