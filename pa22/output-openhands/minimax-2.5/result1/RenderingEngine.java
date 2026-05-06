package game;

/**
 * Interface for rendering engine that displays the game.
 */
public interface RenderingEngine {
    /**
     * Render the game state.
     */
    void render(GameState state);

    /**
     * Print a message to the output.
     */
    void message(String content);
}