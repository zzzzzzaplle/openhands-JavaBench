import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages the dynamic game state including entity positions, undo history, and win condition.
 * Processes actions and maintains checkpoint-based undo history.
 */
public class GameState {

    private final GameMap gameMap;
    private int remainingUndoQuota;

    // Stack of checkpoints; each checkpoint is a list of transitions to revert
    private final Deque<List<Transition>> checkpointStack;

    // Accumulated transitions since the last checkpoint
    private final List<Transition> pendingTransitions;

    /**
     * Creates a GameState for the given game map.
     *
     * @param gameMap the game map
     */
    public GameState(GameMap gameMap) {
        this.gameMap = gameMap;
        this.checkpointStack = new ArrayDeque<>();
        this.pendingTransitions = new ArrayList<>();
        this.remainingUndoQuota = gameMap.getRawUndoLimit();
    }

    /**
     * Processes a game action and returns the result.
     *
     * @param action the action to process
     * @return the result of processing
     */
    public ActionResult processAction(Action action) {
        if (action instanceof Exit) {
            return new Success(action);
        }
        if (action instanceof Undo) {
            return processUndo((Undo) action);
        }
        if (action instanceof Move) {
            return processMove((Move) action);
        }
        return new Failed(action, "Unknown action.");
    }

    /**
     * Checks whether the win condition is met: every destination is occupied by a Box.
     *
     * @return true if all destinations have boxes
     */
    public boolean isWin() {
        for (Position dest : gameMap.getDestinations()) {
            final Entity entity = gameMap.getEntity(dest);
            if (!(entity instanceof Box)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the underlying game map.
     *
     * @return the game map
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Returns the remaining undo quota.
     *
     * @return remaining undo count, or -1 if unlimited
     */
    public int getRemainingUndoQuota() {
        return remainingUndoQuota;
    }

    private ActionResult processMove(Move move) {
        final int playerId = move.getInitiator();

        // Find the player on the map
        final Player player = findPlayer(playerId);
        if (player == null) {
            return new Failed(move, StringResources.PLAYER_NOT_FOUND);
        }

        // Find the player's current position
        final Position playerPos = findEntityPosition(player);
        if (playerPos == null) {
            return new Failed(move, StringResources.PLAYER_NOT_FOUND);
        }

        final Position nextPos = move.nextPosition(playerPos);
        final Entity nextEntity = gameMap.getEntity(nextPos);

        if (nextEntity == null || nextEntity instanceof Wall) {
            return new Failed(move, "Blocked by wall.");
        }

        if (nextEntity instanceof Player) {
            return new Failed(move, "Blocked by another player.");
        }

        if (nextEntity instanceof Box) {
            final Box box = (Box) nextEntity;
            if (box.getPlayerId() != playerId) {
                return new Failed(move, "Cannot push another player's box.");
            }

            // Calculate position behind the box
            final Position behindPos = move.nextPosition(nextPos);
            final Entity behindEntity = gameMap.getEntity(behindPos);

            if (behindEntity == null || !(behindEntity instanceof Empty)) {
                return new Failed(move, "Cannot push box - blocked.");
            }

            // Record transitions for box push
            final List<Transition> transitions = new ArrayList<>();
            transitions.add(new Transition(playerPos, player));
            transitions.add(new Transition(nextPos, box));
            transitions.add(new Transition(behindPos, behindEntity));

            // Apply transitions
            gameMap.putEntity(playerPos, new Empty());
            gameMap.putEntity(nextPos, player);
            gameMap.putEntity(behindPos, box);

            // A box was pushed - this is a checkpoint
            flushPendingTransitions();
            checkpointStack.push(transitions);

            if (isWin()) {
                return new Success(move);
            }
            return new Success(move);
        }

        if (nextEntity instanceof Empty) {
            // Simple move without pushing a box
            final List<Transition> transitions = new ArrayList<>();
            transitions.add(new Transition(playerPos, player));
            transitions.add(new Transition(nextPos, nextEntity));

            // Apply transitions
            gameMap.putEntity(playerPos, new Empty());
            gameMap.putEntity(nextPos, player);

            // Accumulate transitions (no checkpoint since no box was pushed)
            pendingTransitions.addAll(transitions);

            if (isWin()) {
                return new Success(move);
            }
            return new Success(move);
        }

        return new Failed(move, "Cannot move there.");
    }

    private ActionResult processUndo(Undo undo) {
        if (checkpointStack.isEmpty()) {
            return new Failed(undo, "Nothing to undo.");
        }

        // Consume undo quota only if finite and there is a checkpoint
        if (remainingUndoQuota > 0) {
            remainingUndoQuota--;
        } else if (remainingUndoQuota == 0) {
            return new Failed(undo, StringResources.UNDO_QUOTA_RUN_OUT);
        }
        // If remainingUndoQuota == -1, unlimited, no consumption needed

        final List<Transition> checkpoint = checkpointStack.pop();

        // Revert transitions in reverse order
        for (int i = checkpoint.size() - 1; i >= 0; i--) {
            final Transition t = checkpoint.get(i);
            gameMap.putEntity(t.position, t.entity);
        }

        return new Success(undo);
    }

    /**
     * Pushes accumulated pending transitions as a new checkpoint to the history stack.
     */
    private void flushPendingTransitions() {
        if (!pendingTransitions.isEmpty()) {
            final List<Transition> accumulatedCheckpoint = new ArrayList<>(pendingTransitions);
            checkpointStack.push(accumulatedCheckpoint);
            pendingTransitions.clear();
        }
    }

    private Player findPlayer(int playerId) {
        for (Entity entity : getAllEntities()) {
            if (entity instanceof Player && ((Player) entity).getId() == playerId) {
                return (Player) entity;
            }
        }
        return null;
    }

    private Position findEntityPosition(Entity target) {
        for (Map.Entry<Position, Entity> entry : getAllEntries()) {
            if (entry.getValue() == target) {
                return entry.getKey();
            }
        }
        return null;
    }

    private Set<Map.Entry<Position, Entity>> getAllEntries() {
        // Access the internal map - we know the structure
        // Use the GameMap's internal representation via the map itself
        return getEntityEntrySet();
    }

    private Set<Map.Entry<Position, Entity>> getEntityEntrySet() {
        final Set<Map.Entry<Position, Entity>> entries = new HashSet<>();
        for (int y = 0; y < gameMap.getMaxHeight(); y++) {
            for (int x = 0; x < gameMap.getMaxWidth(); x++) {
                final Position pos = Position.of(x, y);
                final Entity entity = gameMap.getEntity(pos);
                if (entity != null) {
                    entries.add(new AbstractMap.SimpleEntry<>(pos, entity));
                }
            }
        }
        return entries;
    }

    private Iterable<Entity> getAllEntities() {
        final List<Entity> entities = new ArrayList<>();
        for (int y = 0; y < gameMap.getMaxHeight(); y++) {
            for (int x = 0; x < gameMap.getMaxWidth(); x++) {
                final Entity entity = gameMap.getEntity(Position.of(x, y));
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    /**
     * Records a change to a single position: which entity was there before the change.
     */
    private static class Transition {
        final Position position;
        final Entity entity;

        Transition(Position position, Entity entity) {
            this.position = position;
            this.entity = entity;
        }
    }
}
