import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the static layout of a Sokoban game map. Contains the grid of
 * entities, destination positions, and the undo limit. The map is parsed from
 * a text representation.
 */
public class GameMap {

    private final Map<Position, Entity> map;
    private final int maxWidth;
    private final int maxHeight;
    private final Set<Position> destinations;
    private final int undoLimit;

    /**
     * Create a new empty game map with the given dimensions.
     *
     * @param maxWidth     the width of the map
     * @param maxHeight    the height of the map
     * @param destinations the set of destination positions
     * @param undoLimit    the undo limit (-1 for unlimited, 0 for disabled)
     */
    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.destinations = Collections.unmodifiableSet(destinations);
        this.undoLimit = undoLimit;
        this.map = new HashMap<>();
    }

    private GameMap(Map<Position, Entity> map, Set<Position> destinations, int undoLimit) {
        this.map = Collections.unmodifiableMap(map);
        this.destinations = Collections.unmodifiableSet(destinations);
        this.undoLimit = undoLimit;
        this.maxWidth = map.keySet().stream().mapToInt(Position::x).max().orElse(0) + 1;
        this.maxHeight = map.keySet().stream().mapToInt(Position::y).max().orElse(0) + 1;
    }

    /**
     * Parse a text representation of a game map.
     * <p>
     * Format: first line is the undo limit, followed by the grid where
     * '#' = wall, '.' = empty, '@' = destination, A-Z = players, a-z = boxes.
     *
     * @param mapText the text representation of the map
     * @return the parsed GameMap
     * @throws IllegalArgumentException if the map is invalid
     */
    public static GameMap parse(String mapText) {
        String[] lines = mapText.split("\n");
        if (lines.length < 2) {
            throw new IllegalArgumentException("Map must have at least an undo limit line and one grid line");
        }

        int undoLimit = Integer.parseInt(lines[0].trim());

        int maxHeight = lines.length - 1;
        int maxWidth = 0;
        for (int i = 1; i < lines.length; i++) {
            maxWidth = Math.max(maxWidth, lines[i].length());
        }

        Map<Position, Entity> map = new HashMap<>();
        Set<Position> destinations = new HashSet<>();
        Set<Integer> playerIds = new HashSet<>();
        int boxCount = 0;

        for (int y = 0; y < maxHeight; y++) {
            String line = lines[y + 1];
            for (int x = 0; x < maxWidth; x++) {
                char c = x < line.length() ? line.charAt(x) : '#';
                Position pos = Position.of(x, y);

                switch (c) {
                    case '#':
                        map.put(pos, new Wall());
                        break;
                    case '.':
                        break;
                    case '@':
                        destinations.add(pos);
                        break;
                    case 'A': case 'B': case 'C': case 'D': case 'E':
                    case 'F': case 'G': case 'H': case 'I': case 'J':
                    case 'K': case 'L': case 'M': case 'N': case 'O':
                    case 'P': case 'Q': case 'R': case 'S': case 'T':
                    case 'U': case 'V': case 'W': case 'X': case 'Y':
                    case 'Z':
                        int playerId = c - 'A';
                        playerIds.add(playerId);
                        map.put(pos, new Player(playerId));
                        break;
                    case 'a': case 'b': case 'c': case 'd': case 'e':
                    case 'f': case 'g': case 'h': case 'i': case 'j':
                    case 'k': case 'l': case 'm': case 'n': case 'o':
                    case 'p': case 'q': case 'r': case 's': case 't':
                    case 'u': case 'v': case 'w': case 'x': case 'y':
                    case 'z':
                        int boxPlayerId = c - 'a';
                        map.put(pos, new Box(boxPlayerId));
                        boxCount++;
                        break;
                    default:
                        // Treat unknown characters as empty
                        break;
                }
            }
        }

        // Validate closed boundary
        for (int x = 0; x < maxWidth; x++) {
            if (!(map.get(Position.of(x, 0)) instanceof Wall)) {
                throw new IllegalArgumentException("Map top boundary is not closed");
            }
            if (!(map.get(Position.of(x, maxHeight - 1)) instanceof Wall)) {
                throw new IllegalArgumentException("Map bottom boundary is not closed");
            }
        }
        for (int y = 0; y < maxHeight; y++) {
            if (!(map.get(Position.of(0, y)) instanceof Wall)) {
                throw new IllegalArgumentException("Map left boundary is not closed");
            }
            if (!(map.get(Position.of(maxWidth - 1, y)) instanceof Wall)) {
                throw new IllegalArgumentException("Map right boundary is not closed");
            }
        }

        // Validate at least one player
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException("Map must have at least one player");
        }

        // Validate destinations count matches boxes count
        if (destinations.size() != boxCount) {
            throw new IllegalArgumentException(
                "Number of destinations (" + destinations.size()
                    + ") must equal number of boxes (" + boxCount + ")"
            );
        }

        // Validate boxes reference valid player IDs
        for (Map.Entry<Position, Entity> entry : map.entrySet()) {
            if (entry.getValue() instanceof Box) {
                int boxPlayerId = ((Box) entry.getValue()).getPlayerId();
                if (!playerIds.contains(boxPlayerId)) {
                    throw new IllegalArgumentException(
                        "Box with playerId " + boxPlayerId + " references non-existent player"
                    );
                }
            }
        }

        return new GameMap(map, destinations, undoLimit);
    }

    /**
     * Get the entity at the specified position.
     *
     * @param position the position to query
     * @return the entity at the position, or null if empty
     */
    public Entity getEntity(Position position) {
        return map.get(position);
    }

    /**
     * Place an entity at the specified position.
     *
     * @param position the position to place the entity
     * @param entity   the entity to place
     */
    public void putEntity(Position position, Entity entity) {
        map.put(position, entity);
    }

    /**
     * @return an unmodifiable set of destination positions
     */
    public Set<Position> getDestinations() {
        return destinations;
    }

    /**
     * Get the undo limit for this map.
     *
     * @return Optional containing the undo limit if limited, or empty if unlimited
     */
    public Optional<Integer> getUndoLimit() {
        if (undoLimit == -1) {
            return Optional.empty();
        }
        return Optional.of(undoLimit);
    }

    /**
     * Get the raw undo limit value.
     *
     * @return the undo limit (-1 for unlimited, 0 for disabled, positive for finite)
     */
    public int getRawUndoLimit() {
        return undoLimit;
    }

    /**
     * @return a set of all player IDs present in the map
     */
    public Set<Integer> getPlayerIds() {
        Set<Integer> ids = new HashSet<>();
        for (Entity entity : map.values()) {
            if (entity instanceof Player) {
                ids.add(((Player) entity).getId());
            }
        }
        return Collections.unmodifiableSet(ids);
    }

    /**
     * @return the width of the map
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * @return the height of the map
     */
    public int getMaxHeight() {
        return maxHeight;
    }
}
