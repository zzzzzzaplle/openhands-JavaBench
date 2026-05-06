package game;

/**
 * Abstract base class for Sokoban games.
 */
public abstract class AbstractSokobanGame implements SokobanGame {
    protected GameState state;
    protected boolean isExitSpecified;

    protected AbstractSokobanGame(GameState gameState) {
        this.state = gameState;
        this.isExitSpecified = false;
    }

    /**
     * Check if the game should stop.
     */
    protected boolean shouldStop() {
        return isExitSpecified || state.isWin();
    }

    /**
     * Process an action and return the result.
     */
    protected ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            isExitSpecified = true;
            return new Success(action);
        }

        if (action instanceof InvalidInput) {
            return new Failed(action, ((InvalidInput) action).getMessage());
        }

        if (action instanceof Undo) {
            if (state.undoToCheckpoint()) {
                return new Success(action);
            } else {
                return new Failed(action, "No more undos available");
            }
        }

        if (action instanceof Move) {
            Move move = (Move) action;
            int playerId = move.getInitiator();
            
            // Find player position
            Position playerPos = state.findPlayer(playerId);
            if (playerPos == null) {
                return new Failed(action, "Player not found");
            }

            // Calculate next position
            Position nextPos = move.nextPosition(playerPos);
            
            // Check if next position is valid
            Entity nextEntity = state.getEntity(nextPos);
            if (nextEntity instanceof Wall) {
                return new Failed(action, "Blocked by wall");
            }

            if (nextEntity instanceof Player) {
                return new Failed(action, "Blocked by player");
            }

            if (nextEntity instanceof Box) {
                // Check if this box belongs to the player
                if (((Box) nextEntity).getPlayerId() != playerId) {
                    return new Failed(action, "Cannot push other player's box");
                }

                // Try to push the box
                Position boxNextPos = move.nextPosition(nextPos);
                Entity boxNextEntity = state.getEntity(boxNextPos);

                if (boxNextEntity instanceof Wall || boxNextEntity instanceof Player || boxNextEntity instanceof Box) {
                    return new Failed(action, "Cannot push box");
                }

                // Push the box
                state.setEntity(boxNextPos, nextEntity);
                state.setEntity(nextPos, new Player(playerId));
                state.saveCheckpoint();
                
                return new Success(action);
            }

            // Empty space - move player
            state.setEntity(nextPos, new Player(playerId));
            
            return new Success(action);
        }

        return new Failed(action, "Unknown action");
    }
}