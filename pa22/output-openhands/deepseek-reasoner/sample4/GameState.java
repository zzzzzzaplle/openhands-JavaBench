import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the dynamic state of the Sokoban game, including entity positions,
 * undo history, and win condition checking.
 */
public class GameState {

    private final GameMap gameMap;
    private final Map<Position, Entity> entities;
    private final Map<Integer, Position> playerPositions;
    private final Deque<List<Transition>> checkpoints;
    private final int undoLimit;
    private int remainingUndos;

    /**
     * Create a new game state from the given game map.
     *
     * @param gameMap the parsed game map with initial layout
     */
    public GameState(GameMap gameMap) {
        this.gameMap = gameMap;
        this.entities = new HashMap<>(gameMap.getMap());
        this.playerPositions = new HashMap<>();
        this.checkpoints = new ArrayDeque<>();
        this.undoLimit = gameMap.getRawUndoLimit();

        if (undoLimit == -1) {
            this.remainingUndos = Integer.MAX_VALUE; // effectively unlimited
        } else {
            this.remainingUndos = undoLimit;
        }

        // Scan for initial player positions
        for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
            Entity entity = entry.getValue();
            if (entity instanceof Player player) {
                playerPositions.put(player.getId(), entry.getKey());
            }
        }
    }

    /**
     * Process a move action.
     *
     * @param move the move action to process
     * @return the result of the move
     */
    public ActionResult processMove(Move move) {
        int playerId = move.getInitiator();
        Position playerPos = playerPositions.get(playerId);

        if (playerPos == null) {
            return new Failed(move, StringResources.PLAYER_NOT_FOUND);
        }

        Position nextPos = move.nextPosition(playerPos);
        Entity entityAtNext = entities.get(nextPos);

        if (entityAtNext instanceof Wall) {
            return new Failed(move, "Blocked by wall");
        }

        if (entityAtNext instanceof Box box) {
            if (box.getPlayerId() != playerId) {
                return new Failed(move, "Cannot push another player's box");
            }

            Position behindPos = move.nextPosition(nextPos);
            Entity entityBehind = entities.get(behindPos);

            if (!(entityBehind instanceof Empty)) {
                return new Failed(move, "Cannot push box, space is occupied");
            }

            // Push the box — record transitions and commit as checkpoint
            List<Transition> transitions = new ArrayList<>();
            transitions.add(new Transition(playerPos, entities.get(playerPos)));
            transitions.add(new Transition(nextPos, entities.get(nextPos)));
            transitions.add(new Transition(behindPos, entities.get(behindPos)));

            entities.put(playerPos, new Empty());
            entities.put(nextPos, new Player(playerId));
            entities.put(behindPos, box);
            playerPositions.put(playerId, nextPos);

            checkpoints.push(transitions);

            return new Success(move);
        }

        if (entityAtNext instanceof Empty) {
            // Simple move — record transitions but do not checkpoint
            List<Transition> transitions = new ArrayList<>();
            transitions.add(new Transition(playerPos, entities.get(playerPos)));
            transitions.add(new Transition(nextPos, entities.get(nextPos)));

            entities.put(playerPos, new Empty());
            entities.put(nextPos, new Player(playerId));
            playerPositions.put(playerId, nextPos);

            // No checkpoint for simple moves
            return new Success(move);
        }

        if (entityAtNext instanceof Player) {
            return new Failed(move, "Blocked by another player");
        }

        return new Failed(move, "Cannot move there");
    }

    /**
     * Process an undo action.
     *
     * @param undo the undo action
     * @return the result of the undo
     */
    public ActionResult processUndo(Undo undo) {
        if (checkpoints.isEmpty()) {
            return new Failed(undo, "Nothing to undo");
        }

        if (undoLimit == 0) {
            return new Failed(undo, "Undo is not allowed");
        }

        if (remainingUndos <= 0) {
            return new Failed(undo, StringResources.UNDO_QUOTA_RUN_OUT);
        }

        // Pop the last checkpoint and revert all transitions
        List<Transition> transitions = checkpoints.pop();
        for (Transition t : transitions) {
            entities.put(t.position(), t.entity());
        }

        // Rebuild player positions after revert
        rebuildPlayerPositions();

        // Consume quota only for finite limits
        if (undoLimit > 0) {
            remainingUndos--;
        }

        return new Success(undo);
    }

    /**
     * Rebuild the player position map from the current entity state.
     */
    private void rebuildPlayerPositions() {
        playerPositions.clear();
        for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
            Entity entity = entry.getValue();
            if (entity instanceof Player player) {
                playerPositions.put(player.getId(), entry.getKey());
            }
        }
    }

    /**
     * Check if the game has been won.
     *
     * @return true if every destination position is occupied by a box
     */
    public boolean isWin() {
        for (Position dest : gameMap.getDestinations()) {
            Entity entity = entities.get(dest);
            if (!(entity instanceof Box)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the game map (static layout)
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * @return the current entity map (mutable view for rendering)
     */
    public Map<Position, Entity> getEntities() {
        return entities;
    }

    /**
     * Get the current position of a player.
     *
     * @param playerId the player ID
     * @return the player's position, or null if not found
     */
    public Position getPlayerPosition(int playerId) {
        return playerPositions.get(playerId);
    }

    /**
     * @return the remaining undo count
     */
    public int getRemainingUndos() {
        return remainingUndos;
    }

    /**
     * @return the undo limit (-1 for unlimited)
     */
    public int getUndoLimit() {
        return undoLimit;
    }

    /**
     * Record of a single state change: the entity that was at a position before a change.
     */
    private record Transition(Position position, Entity entity) {
    }
}
