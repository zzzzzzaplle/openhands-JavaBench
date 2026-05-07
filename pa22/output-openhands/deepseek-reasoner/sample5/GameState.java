import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the dynamic state of a Sokoban game. Maintains the current
 * positions of all moveable entities (players and boxes), processes actions
 * (move, undo), tracks undo history via transitions and checkpoints, and
 * checks win conditions.
 */
public class GameState {

    private final GameMap gameMap;
    private final Map<Position, Entity> entities;

    private final Deque<List<Transition>> checkpoints;
    private int undoQuotaRemaining;

    /**
     * Create a new game state initialized from the given game map.
     *
     * @param gameMap the static game map layout
     */
    public GameState(GameMap gameMap) {
        this.gameMap = gameMap;
        this.entities = new HashMap<>();

        // Copy moveable entities (players and boxes) from the static map
        for (int y = 0; y < gameMap.getMaxHeight(); y++) {
            for (int x = 0; x < gameMap.getMaxWidth(); x++) {
                Position pos = Position.of(x, y);
                Entity e = gameMap.getEntity(pos);
                if (e instanceof Player || e instanceof Box) {
                    entities.put(pos, e);
                }
            }
        }

        this.checkpoints = new ArrayDeque<>();
        this.undoQuotaRemaining = gameMap.getRawUndoLimit();
    }

    /**
     * @return the static game map
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Get the entity at the specified position. Checks the dynamic entity state
     * first (players and boxes that move), then falls back to the static map
     * for immovable entities (walls).
     *
     * @param pos the position to query
     * @return the entity at the position, or null if empty
     */
    public Entity getEntity(Position pos) {
        Entity e = entities.get(pos);
        if (e != null) {
            return e;
        }
        Entity staticEntity = gameMap.getEntity(pos);
        if (staticEntity instanceof Wall) {
            return staticEntity;
        }
        return null;
    }

    /**
     * Process an action and return the result.
     *
     * @param action the action to process
     * @return the result of processing the action
     */
    public ActionResult processAction(Action action) {
        if (action instanceof Move) {
            return processMove((Move) action);
        } else if (action instanceof Undo) {
            return processUndo((Undo) action);
        } else if (action instanceof InvalidInput) {
            return new Failed(action, ((InvalidInput) action).getMessage());
        }
        return new Failed(action, "Unknown action");
    }

    /**
     * Process a move action: attempt to move the player in the specified direction.
     * The player can move into empty spaces or push their own box if the space
     * behind the box is clear.
     */
    private ActionResult processMove(Move move) {
        int initiator = move.getInitiator();

        Position playerPos = findPlayer(initiator);
        if (playerPos == null) {
            return new Failed(move, StringResources.PLAYER_NOT_FOUND);
        }

        Position nextPos = move.nextPosition(playerPos);

        // Check if blocked by a wall
        if (gameMap.getEntity(nextPos) instanceof Wall) {
            return new Failed(move, "Movement blocked by wall");
        }

        Entity nextEntity = entities.get(nextPos);

        // Check if blocked by another player
        if (nextEntity instanceof Player) {
            return new Failed(move, "Movement blocked by another player");
        }

        if (nextEntity instanceof Box) {
            Box box = (Box) nextEntity;
            if (box.getPlayerId() != initiator) {
                return new Failed(move, "Cannot push another player's box");
            }

            Position behindPos = move.nextPosition(nextPos);

            // Check behind the box
            if (gameMap.getEntity(behindPos) instanceof Wall) {
                return new Failed(move, "Cannot push box into wall");
            }
            if (entities.get(behindPos) != null) {
                return new Failed(move, "Cannot push box - space behind is occupied");
            }

            // Push box and move player
            Entity player = entities.get(playerPos);
            entities.remove(playerPos);
            entities.remove(nextPos);
            entities.put(nextPos, player);
            entities.put(behindPos, box);

            // Record checkpoint for undo
            List<Transition> checkpoint = new ArrayList<>();
            checkpoint.add(new Transition(player, playerPos, nextPos));
            checkpoint.add(new Transition(box, nextPos, behindPos));
            checkpoints.push(checkpoint);

            return new Success(move);
        }

        // Empty space - just move the player
        Entity player = entities.get(playerPos);
        entities.remove(playerPos);
        entities.put(nextPos, player);
        return new Success(move);
    }

    /**
     * Process an undo action: revert the most recent checkpoint (box push).
     * Transitions are reverted atomically. All "to" positions are cleared
     * first, then all entities are restored to their original "from" positions,
     * preventing conflicts when one entity's "to" overlaps another's "from".
     */
    private ActionResult processUndo(Undo undo) {
        if (checkpoints.isEmpty()) {
            return new Failed(undo, "Nothing to undo");
        }

        if (undoQuotaRemaining == 0) {
            return new Failed(undo, StringResources.UNDO_QUOTA_RUN_OUT);
        }

        List<Transition> transitions = checkpoints.pop();

        // First pass: clear all destination positions
        for (Transition t : transitions) {
            entities.remove(t.to);
        }
        // Second pass: restore entities to their original positions
        for (Transition t : transitions) {
            entities.put(t.from, t.entity);
        }

        // Consume quota if finite
        if (undoQuotaRemaining > 0) {
            undoQuotaRemaining--;
        }

        return new Success(undo);
    }

    /**
     * Check if the game has been won (all destinations occupied by boxes).
     *
     * @return true if all destinations have a box on them
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
     * Get the remaining undo quota.
     *
     * @return the number of undos remaining (-1 for unlimited, 0 for none)
     */
    public int getUndoQuotaRemaining() {
        return undoQuotaRemaining;
    }

    /**
     * Find the position of the player with the given ID.
     *
     * @param playerId the player ID to find
     * @return the player's position, or null if not found
     */
    private Position findPlayer(int playerId) {
        for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
            if (entry.getValue() instanceof Player) {
                Player p = (Player) entry.getValue();
                if (p.getId() == playerId) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Records the movement of an entity from one position to another,
     * used for undo functionality.
     */
    private static class Transition {
        final Entity entity;
        final Position from;
        final Position to;

        Transition(Entity entity, Position from, Position to) {
            this.entity = entity;
            this.from = from;
            this.to = to;
        }
    }
}
