package assignment.game;

/**
 * Terminal-based Sokoban game that coordinates input, rendering, and game logic.
 * Supports up to 2 players in the terminal interface.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {
    private final InputEngine inputEngine;
    private final RenderingEngine renderingEngine;

    /**
     * Create a terminal Sokoban game.
     *
     * @param gameState        the initial game state
     * @param inputEngine      the input engine for reading user commands
     * @param renderingEngine  the rendering engine for displaying the game
     */
    public TerminalSokobanGame(GameState gameState, TerminalInputEngine inputEngine,
                                TerminalRenderingEngine renderingEngine) {
        super(gameState);
        this.inputEngine = inputEngine;
        this.renderingEngine = renderingEngine;
    }

    @Override
    public void run() {
        renderingEngine.message(StringResources.GAME_READY_MESSAGE);
        renderingEngine.render(state);

        while (!shouldStop()) {
            Action action = inputEngine.fetchAction();
            ActionResult result = processAction(action);

            if (result instanceof Failed && action instanceof InvalidInput) {
                renderingEngine.message(((Failed) result).getReason());
            }

            if (!(action instanceof InvalidInput)) {
                renderingEngine.render(state);
            }

            if (state.isWin()) {
                renderingEngine.message(StringResources.WIN_MESSAGE);
                break;
            }
        }

        if (state.isWin()) {
            // Win message already printed above
        } else {
            renderingEngine.message(StringResources.GAME_EXIT_MESSAGE);
        }
    }
}
