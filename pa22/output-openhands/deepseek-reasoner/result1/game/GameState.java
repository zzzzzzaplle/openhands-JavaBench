package assignment.game;

import java.util.*;

/**
 * Represents the dynamic state of a Sokoban game.
 * Tracks current positions of players and boxes, manages undo history,
 * and processes game actions.
 */
public class GameState {
    private final GameMap gameMap;
    private final Map<Integer, Position> playerPositions;
    private final Map<Integer, Position> boxPositions;
    private final Deque<List<Transition>> checkpointStack;
    private int remainingUndoQuota;

    /**
     * Create a new game state from the given game map.
     *
     * @param gameMap the parsed game map
     */
    public GameState(GameMap gameMap) {
        this.gameMap = gameMap;
        this.playerPositions = new HashMap<>();
        this.boxPositions = new HashMap<>();
        this.checkpointStack = new ArrayDeque<>();
        this.remainingUndoQuota = gameMap.getRawUndoLimit();

        // Initialize positions from the static map
        for (Map.Entry<Position, Entity> entry : gameMap.getEntities().entrySet()) {
            Position pos = entry.getKey();
            Entity entity = entry.getValue();
            if (entity instanceof Player) {
                playerPositions.put(((Player) entity).getId(), pos);
            } else if (entity instanceof Box) {
                boxPositions.put(((Box) entity).getPlayerId(), pos);
            }
        }
    }

    /**
     * @return the game map
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Get the entity at the given position in the current state.
     *
     * @param position the position to check
     * @return the entity at that position
     */
    public Entity getEntity(Position position) {
        // Check dynamic entities first
        for (Map.Entry<Integer, Position> entry : playerPositions.entrySet()) {
            if (entry.getValue().equals(position)) {
                return new Player(entry.getKey());
            }
        }
        for (Map.Entry<Integer, Position> entry : boxPositions.entrySet()) {
            if (entry.getValue().equals(position)) {
                return new Box(entry.getKey());
            }
        }
        // Fall back to static map
        Entity staticEntity = gameMap.getEntity(position);
        return staticEntity != null ? staticEntity : new Empty();
    }

    /**
     * Check whether a position is empty (not a wall, not occupied by a player or box).
     *
     * @param position the position to check
     * @return true if the position is empty
     */
    private boolean isEmpty(Position position) {
        Entity entity = gameMap.getEntity(position);
        if (entity instanceof Wall) {
            return false;
        }
        for (Position p : playerPositions.values()) {
            if (p.equals(position)) return false;
        }
        for (Position p : boxPositions.values()) {
            if (p.equals(position)) return false;
        }
        return true;
    }

    /**
     * Find which box (if any) is at the given position.
     *
     * @param position the position to check
     * @return the playerId of the box at the position, or -1 if none
     */
    private int getBoxAt(Position position) {
        for (Map.Entry<Integer, Position> entry : boxPositions.entrySet()) {
            if (entry.getValue().equals(position)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Find which player (if any) is at the given position.
     *
     * @param position the position to check
     * @return the player ID at the position, or -1 if none
     */
    private int getPlayerAt(Position position) {
        for (Map.Entry<Integer, Position> entry : playerPositions.entrySet()) {
            if (entry.getValue().equals(position)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * @return the current undo quota remaining
     */
    public int getRemainingUndoQuota() {
        return remainingUndoQuota;
    }

    /**
     * @return the checkpoint stack (for undo)
     */
    Deque<List<Transition>> getCheckpointStack() {
        return checkpointStack;
    }

    /**
     * Process a move action.
     *
     * @param move the move action to process
     * @return the result of the move
     */
    public ActionResult processMove(Move move) {
        int playerId = move.getInitiator();
        Position currentPos = playerPositions.get(playerId);
        if (currentPos == null) {
            return new Failed(move, StringResources.PLAYER_NOT_FOUND);
        }

        Position nextPos = move.nextPosition(currentPos);

        // Check what's at the next position
        Entity nextEntity = gameMap.getEntity(nextPos);
        if (nextEntity instanceof Wall) {
            return new Failed(move, "Blocked by wall");
        }

        // Check if another player is at the next position
        if (getPlayerAt(nextPos) != -1) {
            return new Failed(move, "Blocked by another player");
        }

        // Collect transitions for this move
        List<Transition> transitions = new ArrayList<>();

        // Check if a box is at the next position
        int boxPlayerId = getBoxAt(nextPos);
        if (boxPlayerId != -1) {
            // Box can only be pushed by its owner
            if (boxPlayerId != playerId) {
                return new Failed(move, "Cannot push another player's box");
            }

            Position pushPos = move.nextPosition(nextPos);

            // Check if push position is empty
            if (!isEmpty(pushPos)) {
                return new Failed(move, "Cannot push box, space behind is occupied");
            }

            // Push the box
            transitions.add(new Transition(boxPlayerId, nextPos, pushPos, Transition.Type.BOX));
            boxPositions.put(boxPlayerId, pushPos);

            // Move the player to where the box was
            transitions.add(new Transition(playerId, currentPos, nextPos, Transition.Type.PLAYER));
            playerPositions.put(playerId, nextPos);

            // Record checkpoint (box was pushed)
            checkpointStack.push(transitions);
        } else {
            // Move player to empty space
            transitions.add(new Transition(playerId, currentPos, nextPos, Transition.Type.PLAYER));
            playerPositions.put(playerId, nextPos);
        }

        return new Success(move);
    }

    /**
     * Process an undo action.
     *
     * @param undo the undo action
     * @return the result of the undo
     */
    public ActionResult processUndo(Undo undo) {
        if (remainingUndoQuota == 0) {
            return new Failed(undo, StringResources.UNDO_QUOTA_RUN_OUT);
        }

        if (checkpointStack.isEmpty()) {
            return new Failed(undo, "Nothing to undo");
        }

        List<Transition> transitions = checkpointStack.pop();

        // Revert transitions in reverse order
        for (int i = transitions.size() - 1; i >= 0; i--) {
            Transition t = transitions.get(i);
            if (t.type == Transition.Type.PLAYER) {
                playerPositions.put(t.entityId, t.from);
            } else if (t.type == Transition.Type.BOX) {
                boxPositions.put(t.entityId, t.from);
            }
        }

        // Consume undo quota (unless unlimited)
        if (remainingUndoQuota > 0) {
            remainingUndoQuota--;
        }

        return new Success(undo);
    }

    /**
     * Check if the game has been won.
     * The game is won when every destination position is occupied by a box.
     *
     * @return true if all destinations have boxes on them
     */
    public boolean isWin() {
        Set<Position> destinations = gameMap.getDestinations();
        for (Position dest : destinations) {
            if (getBoxAt(dest) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * A record of an entity moving from one position to another.
     */
    static class Transition {
        enum Type { PLAYER, BOX }

        final int entityId;
        final Position from;
        final Position to;
        final Type type;

        Transition(int entityId, Position from, Position to, Type type) {
            this.entityId = entityId;
            this.from = from;
            this.to = to;
            this.type = type;
        }
    }
}
