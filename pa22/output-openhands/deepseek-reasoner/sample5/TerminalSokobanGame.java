/**
 * Terminal-based implementation of the Sokoban game. Coordinates input
 * reading from the terminal, rendering the game board, and running the
 * main game loop. Restricted to a maximum of two players for the TUI.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final InputEngine inputEngine;
    private final RenderingEngine renderingEngine;

    /**
     * Create a new terminal Sokoban game.
     *
     * @param gameState        the initial game state
     * @param inputEngine      the input engine for reading user commands
     * @param renderingEngine  the rendering engine for displaying the board
     */
    public TerminalSokobanGame(GameState gameState, TerminalInputEngine inputEngine,
                                TerminalRenderingEngine renderingEngine) {
        super(gameState);
        this.inputEngine = inputEngine;
        this.renderingEngine = renderingEngine;
    }

    @Override
    public void run() {
        renderingEngine.render(state);
        renderingEngine.message(StringResources.GAME_READY_MESSAGE);

        while (!shouldStop()) {
            Action action = inputEngine.fetchAction();
            ActionResult result = processAction(action);

            if (result instanceof Failed) {
                renderingEngine.message(((Failed) result).getReason());
            }

            if (!shouldStop()) {
                renderingEngine.render(state);
            }
        }

        if (state.isWin()) {
            renderingEngine.render(state);
            renderingEngine.message(StringResources.WIN_MESSAGE);
        }

        renderingEngine.message(StringResources.GAME_EXIT_MESSAGE);
    }
}
