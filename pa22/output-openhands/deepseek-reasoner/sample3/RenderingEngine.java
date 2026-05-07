/**
 * Interface for rendering the game state to an output medium.
 */
public interface RenderingEngine {

    /**
     * Renders the current game state visually.
     *
     * @param state the current game state
     */
    void render(GameState state);

    /**
     * Outputs a message string.
     *
     * @param content the message to display
     */
    void message(String content);
}
