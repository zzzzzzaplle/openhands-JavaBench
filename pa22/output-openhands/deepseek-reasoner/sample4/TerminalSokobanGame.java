/**
 * The main terminal-based Sokoban game runner. Coordinates input, rendering,
 * and game logic for up to two players.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final InputEngine inputEngine;
    private final RenderingEngine renderingEngine;

    /**
     * Create a terminal Sokoban game.
     *
     * @param gameState        the initial game state
     * @param inputEngine      the input engine for reading commands
     * @param renderingEngine  the rendering engine for displaying the board
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

        String quotaDisplay = getUndoQuotaDisplay();
        if (quotaDisplay != null) {
            renderingEngine.message(quotaDisplay);
        }

        while (!shouldStop()) {
            Action action = inputEngine.fetchAction();
            ActionResult result = processAction(action);

            if (result instanceof Failed failed) {
                renderingEngine.message(failed.getReason());
            }

            if (shouldStop()) {
                if (state.isWin()) {
                    renderingEngine.message(StringResources.WIN_MESSAGE);
                } else {
                    renderingEngine.message(StringResources.GAME_EXIT_MESSAGE);
                }
                break;
            }

            renderingEngine.render(state);
        }
    }

    /**
     * Build the undo quota display string based on current game state.
     *
     * @return the quota string, or null if undos are unlimited and quota should not be shown
     */
    private String getUndoQuotaDisplay() {
        int limit = state.getUndoLimit();
        if (limit == -1) {
            return String.format(StringResources.UNDO_QUOTA_TEMPLATE, StringResources.UNDO_QUOTA_UNLIMITED);
        }
        return String.format(StringResources.UNDO_QUOTA_TEMPLATE, state.getRemainingUndos());
    }
}
