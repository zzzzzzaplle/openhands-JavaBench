package game;

import java.util.*;

/**
 * Game state containing the map, entities, and history for undo.
 */
public class GameState {
    private GameMap gameMap;
    private Map<Position, Entity> entities;
    private int undoQuota;
    private List<Map<Position, Entity>> history;
    private List<Map<Position, Entity>> checkpoints;

    public GameState(GameMap gameMap) {
        this.gameMap = gameMap;
        this.entities = new HashMap<>(gameMap.getEntities().stream()
            .collect(java.util.stream.Collectors.toMap(
                e -> findPosition(e).orElse(new Position(0, 0)),
                e -> e
            )));
        
        // Copy entities from map
        for (Position pos : gameMap.getDestinations()) {
            Entity e = gameMap.getEntity(pos);
            if (e != null) {
                entities.put(pos, e);
            }
        }
        
        // Initialize undo quota
        this.undoQuota = gameMap.getUndoLimit().orElse(0);
        
        this.history = new ArrayList<>();
        this.checkpoints = new ArrayList<>();
    }

    private Optional<Position> findPosition(Entity e) {
        for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
            if (entry.getValue() == e) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Entity getEntity(Position position) {
        return entities.get(position);
    }

    public void setEntity(Position position, Entity entity) {
        // Save to history before change
        saveTransition();
        
        // Clear old position if was a player or box
        for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
            if (entry.getValue() == entity) {
                entities.remove(entry.getKey());
                break;
            }
        }
        
        entities.put(position, entity);
    }

    /**
     * Save current state for transition.
     */
    private void saveTransition() {
        history.add(new HashMap<>(entities));
    }

    /**
     * Save a checkpoint (when box is pushed).
     */
    public void saveCheckpoint() {
        checkpoints.add(new HashMap<>(entities));
    }

    /**
     * Undo the last transition.
     */
    public boolean undo() {
        if (!history.isEmpty()) {
            entities = history.remove(history.size() - 1);
            return true;
        }
        return false;
    }

    /**
     * Undo to the last checkpoint.
     */
    public boolean undoToCheckpoint() {
        if (undoQuota == 0) {
            return false;
        }
        
        if (checkpoints.isEmpty()) {
            return false;
        }
        
        if (undoQuota > 0) {
            undoQuota--;
        }
        
        entities = checkpoints.remove(checkpoints.size() - 1);
        return true;
    }

    /**
     * Get remaining undo quota.
     */
    public int getUndoQuota() {
        return undoQuota;
    }

    /**
     * Check if player has won (all destinations have boxes).
     */
    public boolean isWin() {
        for (Position dest : gameMap.getDestinations()) {
            Entity e = entities.get(dest);
            if (!(e instanceof Box)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find the position of a player by ID.
     */
    public Position findPlayer(int playerId) {
        for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
            if (entry.getValue() instanceof Player) {
                if (((Player) entry.getValue()).getId() == playerId) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}