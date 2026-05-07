package assignment.game;

/**
 * The main terminal-based Sokoban game runner.
 * Coordinates the game loop by fetching actions from input, processing them,
 * and rendering the updated state.
 * <p>
 * The terminal interface is restricted to a maximum of two players (player 0 and player 1),
 * although the backend supports up to 26.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final TerminalInputEngine inputEngine;
    private final TerminalRenderingEngine renderingEngine;

    /**
     * Creates a TerminalSokobanGame with the given state, input engine, and rendering engine.
     *
     * @param gameState        the initial game state
     * @param inputEngine      the terminal input engine
     * @param renderingEngine  the terminal rendering engine
     */
    public TerminalSokobanGame(GameState gameState,
                               TerminalInputEngine inputEngine,
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

            // Handle action result feedback
            if (result instanceof Failed failed) {
                renderingEngine.message(failed.getReason());
            }

            if (shouldStop()) {
                if (state.isWin()) {
                    renderingEngine.message(StringResources.WIN_MESSAGE);
                } else if (action instanceof Exit) {
                    renderingEngine.message(StringResources.GAME_EXIT_MESSAGE);
                }
            } else {
                renderingEngine.render(state);
            }
        }
    }
}
