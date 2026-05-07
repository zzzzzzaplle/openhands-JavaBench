package assignment.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages the dynamic state of a Sokoban game, including entity positions,
 * undo history with checkpoints, and win condition detection.
 */
public class GameState {

    private final GameMap gameMap;
    private final Map<Position, Entity> entities;
    private final Map<Integer, Position> playerPositions;
    private final Deque<List<Transition>> checkpointStack;
    private int undoQuotaRemaining;

    /**
     * Creates a new GameState from the given GameMap.
     *
     * @param gameMap the parsed game map
     */
    public GameState(GameMap gameMap) {
        this.gameMap = gameMap;
        this.entities = new HashMap<>();
        this.playerPositions = new HashMap<>();
        this.checkpointStack = new ArrayDeque<>();
        initQuota();

        // Copy entities from the map into mutable storage
        for (int x = 0; x < gameMap.getMaxWidth(); x++) {
            for (int y = 0; y < gameMap.getMaxHeight(); y++) {
                Position pos = Position.of(x, y);
                Entity entity = gameMap.getEntity(pos);
                if (entity != null) {
                    entities.put(pos, entity);
                    if (entity instanceof Player player) {
                        playerPositions.put(player.getId(), pos);
                    }
                }
            }
        }
    }

    private void initQuota() {
        if (gameMap.getUndoLimit().isPresent()) {
            this.undoQuotaRemaining = gameMap.getUndoLimit().get();
        } else {
            this.undoQuotaRemaining = -1; // unlimited
        }
    }

    /**
     * Processes a move action, updating entity positions and recording history.
     *
     * @param move the move action to process
     * @return Success if the move was valid, Failed otherwise
     */
    public ActionResult processMove(Move move) {
        int playerId = move.getInitiator();
        Position currentPos = playerPositions.get(playerId);
        if (currentPos == null) {
            return new Failed(move, StringResources.PLAYER_NOT_FOUND);
        }

        Position nextPos = move.nextPosition(currentPos);
        Entity nextEntity = entities.get(nextPos);

        if (nextEntity instanceof Wall) {
            return new Failed(move, "Cannot move into a wall.");
        }

        if (nextEntity instanceof Player) {
            return new Failed(move, "Cannot move into another player.");
        }

        if (nextEntity instanceof Box box) {
            // Check if box belongs to this player
            if (box.getPlayerId() != playerId) {
                return new Failed(move, "Cannot push another player's box.");
            }

            // Check the space behind the box
            Position behindPos = move.nextPosition(nextPos);
            Entity behindEntity = entities.get(behindPos);

            if (behindEntity != null && !(behindEntity instanceof Empty)) {
                return new Failed(move, "Cannot push box into an occupied space.");
            }

            // Record transitions for undo checkpoint
            List<Transition> transitions = new ArrayList<>();

            // Move player from currentPos to nextPos, leaving Empty behind
            Player player = (Player) entities.get(currentPos);
            entities.remove(currentPos);
            entities.put(currentPos, new Empty());
            transitions.add(new Transition(currentPos, nextPos, player));

            // Move box from nextPos to behindPos
            entities.remove(nextPos);
            entities.put(nextPos, player);
            transitions.add(new Transition(nextPos, behindPos, box));

            // Remove whatever was behind (Empty or nothing)
            if (behindEntity instanceof Empty) {
                entities.remove(behindPos);
            }
            entities.put(behindPos, box);

            playerPositions.put(playerId, nextPos);

            // Record checkpoint since a box was pushed
            checkpointStack.push(transitions);

            return new Success(move);
        }

        // Next position is empty or unoccupied - just move the player
        if (nextEntity instanceof Empty || nextEntity == null) {
            Player player = (Player) entities.get(currentPos);
            entities.remove(currentPos);
            entities.put(currentPos, new Empty());
            if (nextEntity != null) {
                entities.remove(nextPos);
            }
            entities.put(nextPos, player);
            playerPositions.put(playerId, nextPos);

            return new Success(move);
        }

        return new Failed(move, "Cannot move there.");
    }

    /**
     * Processes an undo action, reverting the most recent checkpoint.
     *
     * @param undo the undo action
     * @return Success if undo was performed, Failed otherwise
     */
    public ActionResult processUndo(Undo undo) {
        if (checkpointStack.isEmpty()) {
            return new Failed(undo, "Nothing to undo.");
        }

        // Check undo quota
        if (undoQuotaRemaining == 0) {
            return new Failed(undo, StringResources.UNDO_QUOTA_RUN_OUT);
        }

        // Consume quota if finite
        if (undoQuotaRemaining > 0) {
            undoQuotaRemaining--;
        }

        // Revert all transitions in the checkpoint atomically
        List<Transition> transitions = checkpointStack.pop();
        for (Transition t : transitions) {
            entities.remove(t.to);
            entities.put(t.from, t.entity);
            if (t.entity instanceof Player player) {
                playerPositions.put(player.getId(), t.from);
            }
        }

        return new Success(undo);
    }

    /**
     * Returns whether the game has been won, i.e., every destination
     * position is occupied by a box.
     *
     * @return true if the game is won
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
     * Returns the entity at the given position.
     *
     * @param position the position to query
     * @return the entity at the position, or null if unoccupied
     */
    public Entity getEntity(Position position) {
        return entities.get(position);
    }

    /**
     * Returns the position of the player with the given ID.
     *
     * @param playerId the player ID
     * @return the player's position, or null if not found
     */
    public Position getPlayerPosition(int playerId) {
        return playerPositions.get(playerId);
    }

    /**
     * Returns the underlying GameMap.
     *
     * @return the game map
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Returns the number of checkpoints available for undo.
     *
     * @return the number of available undo checkpoints
     */
    public int getCheckpointCount() {
        return checkpointStack.size();
    }

    /**
     * Returns the remaining undo quota.
     *
     * @return remaining undo count, or -1 if unlimited
     */
    public int getUndoQuotaRemaining() {
        return undoQuotaRemaining;
    }

    /**
     * Returns all positions currently occupied by entities.
     *
     * @return the key set of the entity map
     */
    public Set<Position> getOccupiedPositions() {
        return new HashSet<>(entities.keySet());
    }

    /**
     * Records a single entity movement from one position to another for undo purposes.
     * The {@code from} field is the original (pre-move) position, {@code to} is the
     * position the entity moved to, and {@code entity} is the entity that moved.
     * Reverting puts the entity back at {@code from}.
     */
    private record Transition(Position from, Position to, Entity entity) {
    }
}
