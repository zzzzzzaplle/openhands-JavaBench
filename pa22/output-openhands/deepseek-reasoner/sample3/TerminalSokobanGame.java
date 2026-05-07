/**
 * Terminal-based implementation of the Sokoban game.
 * Coordinates the game loop, processing input from an InputEngine and rendering via a RenderingEngine.
 * Restricted to a maximum of two players in the terminal interface.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final InputEngine inputEngine;
    private final RenderingEngine renderingEngine;

    /**
     * Creates a TerminalSokobanGame.
     *
     * @param gameState        the initial game state
     * @param inputEngine      the engine for reading player input
     * @param renderingEngine  the engine for rendering the game board
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
        displayUndoQuota();

        if (state.isWin()) {
            renderingEngine.message(StringResources.WIN_MESSAGE);
            return;
        }

        while (!shouldStop()) {
            final Action action = inputEngine.fetchAction();

            if (action instanceof InvalidInput) {
                renderingEngine.message(((InvalidInput) action).getMessage());
                continue;
            }

            final ActionResult result = processAction(action);

            if (result instanceof Failed) {
                renderingEngine.message(((Failed) result).getReason());
                continue;
            }

            // Successful action
            if (action instanceof Exit) {
                renderingEngine.message(StringResources.GAME_EXIT_MESSAGE);
                break;
            }

            renderingEngine.render(state);
            displayUndoQuota();

            if (state.isWin()) {
                renderingEngine.message(StringResources.WIN_MESSAGE);
                break;
            }
        }
    }

    private void displayUndoQuota() {
        final int quota = state.getRemainingUndoQuota();
        if (quota == -1) {
            renderingEngine.message(
                StringResources.UNDO_QUOTA_TEMPLATE.formatted(StringResources.UNDO_QUOTA_UNLIMITED)
            );
        } else {
            renderingEngine.message(
                StringResources.UNDO_QUOTA_TEMPLATE.formatted(String.valueOf(quota))
            );
        }
    }
}
