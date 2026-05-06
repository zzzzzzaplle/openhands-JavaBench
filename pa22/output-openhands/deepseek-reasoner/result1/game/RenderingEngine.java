package assignment.game;

/**
 * Interface for rendering the game state and displaying messages.
 */
public interface RenderingEngine {
    /**
     * Render the current game state to the output.
     *
     * @param state the current game state
     */
    void render(GameState state);

    /**
     * Display a message to the output.
     *
     * @param content the message content
     */
    void message(String content);
}
