/**
 * Interface for rendering the game state and displaying messages.
 */
public interface RenderingEngine {

    /**
     * Render the current game state to the output.
     *
     * @param state the current game state to render
     */
    void render(GameState state);

    /**
     * Display a message to the user.
     *
     * @param content the message content
     */
    void message(String content);
}
