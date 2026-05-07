package assignment.game;

/**
 * Interface for rendering the game state and displaying messages to the user.
 */
public interface RenderingEngine {

    /**
     * Renders the current game state as a visual grid.
     *
     * @param state the current game state to render
     */
    void render(GameState state);

    /**
     * Displays a text message to the user.
     *
     * @param content the message to display
     */
    void message(String content);
}
