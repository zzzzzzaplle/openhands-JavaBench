package game;

/**
 * Terminal-based Sokoban game implementation.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {
    private InputEngine inputEngine;
    private RenderingEngine renderingEngine;

    public TerminalSokobanGame(GameState gameState, InputEngine inputEngine, RenderingEngine renderingEngine) {
        super(gameState);
        this.inputEngine = inputEngine;
        this.renderingEngine = renderingEngine;
    }

    @Override
    public void run() {
        renderingEngine.message("Sokoban game is ready.");
        
        while (!shouldStop()) {
            // Render the game
            renderingEngine.render(state);
            
            // Show undo quota
            int quota = state.getUndoQuota();
            if (quota < 0) {
                renderingEngine.message("Undo Quota: Unlimited");
            } else {
                renderingEngine.message("Undo Quota: " + quota);
            }
            
            // Get user input
            Action action = inputEngine.fetchAction();
            
            // Process action
            ActionResult result = processAction(action);
            
            // Show result
            if (result instanceof Success) {
                // Check win condition
                if (state.isWin()) {
                    renderingEngine.render(state);
                    renderingEngine.message("You win.");
                    break;
                }
            } else if (result instanceof Failed) {
                renderingEngine.message(((Failed) result).getReason());
            }
        }
        
        if (isExitSpecified) {
            renderingEngine.message("Game exits.");
        }
    }
}