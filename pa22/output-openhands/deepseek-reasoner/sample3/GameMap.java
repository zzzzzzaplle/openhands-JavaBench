import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the static game board layout including walls, entities, destinations, and the undo limit.
 * The board is a closed-boundary 2D grid parsed from a text representation.
 */
public class GameMap {

    private final Map<Position, Entity> map;
    private final int maxWidth;
    private final int maxHeight;
    private final Set<Position> destinations;
    private final int undoLimit;

    /**
     * Creates a GameMap with specified dimensions and destinations.
     * The entity map starts empty.
     *
     * @param maxWidth     the width of the map
     * @param maxHeight    the height of the map
     * @param destinations the set of destination positions
     * @param undoLimit    the undo limit (-1 for unlimited, 0 for none)
     */
    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.destinations = Collections.unmodifiableSet(destinations);
        this.undoLimit = undoLimit;
        this.map = new HashMap<>();
    }

    private GameMap(Map<Position, Entity> map, Set<Position> destinations, int undoLimit) {
        this.map = new HashMap<>(map);
        this.destinations = Collections.unmodifiableSet(new HashSet<>(destinations));
        this.undoLimit = undoLimit;
        this.maxWidth = map.keySet().stream().mapToInt(Position::x).max().orElse(0) + 1;
        this.maxHeight = map.keySet().stream().mapToInt(Position::y).max().orElse(0) + 1;
    }

    /**
     * Parses a text representation of the game board into a GameMap.
     * <p>
     * Text format: first line is the undo limit, subsequent lines are the grid rows.
     * Characters: '#' = Wall, '@' = Destination, '.' = Empty, A-Z = Players, a-z = Boxes.
     * </p>
     *
     * @param mapText the text representation of the map
     * @return a parsed GameMap
     * @throws IllegalArgumentException if the map is invalid
     */
    public static GameMap parse(String mapText) {
        // Remove trailing whitespace/newlines and split
        final String cleaned = mapText.stripTrailing();
        final String[] lines = cleaned.split("\n");

        // First line is the undo limit
        final int undoLimit = Integer.parseInt(lines[0].trim());

        // Parse the grid (lines after the first)
        final int height = lines.length - 1;
        int width = 0;
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() > width) {
                width = lines[i].length();
            }
        }

        final Map<Position, Entity> map = new HashMap<>();
        final Set<Position> destinations = new HashSet<>();
        final Set<Integer> playerIds = new HashSet<>();
        final Set<Integer> boxPlayerIds = new HashSet<>();

        for (int y = 0; y < height; y++) {
            final String line = lines[y + 1];
            for (int x = 0; x < line.length(); x++) {
                final char ch = line.charAt(x);
                final Position pos = Position.of(x, y);

                switch (ch) {
                    case '#':
                        map.put(pos, new Wall());
                        break;
                    case '@':
                        map.put(pos, new Empty());
                        destinations.add(pos);
                        break;
                    case '.':
                        map.put(pos, new Empty());
                        break;
                    case ' ':
                        // Treat spaces as empty
                        break;
                    default:
                        if (ch >= 'A' && ch <= 'Z') {
                            final int playerId = ch - 'A';
                            map.put(pos, new Player(playerId));
                            playerIds.add(playerId);
                        } else if (ch >= 'a' && ch <= 'z') {
                            final int playerId = ch - 'a';
                            map.put(pos, new Box(playerId));
                            boxPlayerIds.add(playerId);
                        }
                        break;
                }
            }
        }

        // Validation: closed boundary
        for (int x = 0; x < width; x++) {
            if (!(map.get(Position.of(x, 0)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed boundary at top edge");
            }
            if (!(map.get(Position.of(x, height - 1)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed boundary at bottom edge");
            }
        }
        for (int y = 0; y < height; y++) {
            if (!(map.get(Position.of(0, y)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed boundary at left edge");
            }
            if (!(map.get(Position.of(width - 1, y)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed boundary at right edge");
            }
        }

        // Validation: at least one player
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException("Map must have at least one player");
        }

        // Validation: number of destinations equals number of boxes
        if (destinations.size() != boxPlayerIds.size()) {
            throw new IllegalArgumentException(
                "Number of destinations (" + destinations.size()
                    + ") must equal number of boxes (" + boxPlayerIds.size() + ")"
            );
        }

        // Validation: boxes must reference valid player IDs
        for (int boxPlayerId : boxPlayerIds) {
            if (!playerIds.contains(boxPlayerId)) {
                throw new IllegalArgumentException(
                    "Box references player " + boxPlayerId + " but no such player exists"
                );
            }
        }

        // Fill in any missing positions with Empty
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final Position pos = Position.of(x, y);
                if (!map.containsKey(pos)) {
                    map.put(pos, new Empty());
                }
            }
        }

        return new GameMap(map, destinations, undoLimit);
    }

    /**
     * Returns the entity at the given position.
     *
     * @param position the position to query
     * @return the entity at the position, or null if none
     */
    public Entity getEntity(Position position) {
        return map.get(position);
    }

    /**
     * Places an entity at the given position.
     *
     * @param position the position to place the entity at
     * @param entity   the entity to place
     */
    public void putEntity(Position position, Entity entity) {
        map.put(position, entity);
    }

    /**
     * Returns the set of destination positions.
     *
     * @return unmodifiable set of destination positions
     */
    public Set<Position> getDestinations() {
        return destinations;
    }

    /**
     * Returns the undo limit, if finite.
     *
     * @return Optional containing the undo limit if not unlimited, or empty if unlimited
     */
    public Optional<Integer> getUndoLimit() {
        if (undoLimit == -1) {
            return Optional.empty();
        }
        return Optional.of(undoLimit);
    }

    /**
     * Returns the set of all player IDs present on the map.
     *
     * @return set of player IDs
     */
    public Set<Integer> getPlayerIds() {
        final Set<Integer> ids = new HashSet<>();
        for (Entity entity : map.values()) {
            if (entity instanceof Player) {
                ids.add(((Player) entity).getId());
            }
        }
        return Collections.unmodifiableSet(ids);
    }

    /**
     * Returns the width of the map.
     *
     * @return max width
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Returns the height of the map.
     *
     * @return max height
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Returns the raw undo limit value.
     *
     * @return undo limit (-1 = unlimited, 0 = none)
     */
    public int getRawUndoLimit() {
        return undoLimit;
    }
}
