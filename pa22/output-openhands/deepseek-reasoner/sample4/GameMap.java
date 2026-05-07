import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the static layout of the Sokoban game board including walls,
 * destinations, and initial entity positions.
 */
public class GameMap {

    private final Map<Position, Entity> map;
    private final int maxWidth;
    private final int maxHeight;
    private final Set<Position> destinations;
    private final int undoLimit;

    /**
     * Create a GameMap with the specified dimensions and destinations.
     * The entity map starts empty; use {@link #putEntity(Position, Entity)} to populate it.
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
     * Parse a text representation of a Sokoban map.
     * <p>
     * The first line is the undo limit. The remaining lines form the grid where:
     * '#' = Wall, '@' = Destination, '.' = Empty, A-Z = Players, a-z = Boxes.
     *
     * @param mapText the text representation of the map
     * @return the parsed GameMap
     * @throws IllegalArgumentException if the map is invalid
     */
    public static GameMap parse(String mapText) {
        // Normalize line endings and split
        String normalized = mapText.replace("\r\n", "\n").replace("\r", "\n");
        String[] rawLines = normalized.split("\n");

        // Skip trailing empty lines
        int lastNonEmpty = rawLines.length - 1;
        while (lastNonEmpty > 0 && rawLines[lastNonEmpty].isEmpty()) {
            lastNonEmpty--;
        }
        int lineCount = lastNonEmpty + 1;

        if (lineCount < 2) {
            throw new IllegalArgumentException("Map must have at least an undo limit line and one grid line");
        }

        // Parse undo limit from first line
        int undoLimit;
        try {
            undoLimit = Integer.parseInt(rawLines[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid undo limit: " + rawLines[0].trim());
        }

        // Parse grid lines
        int height = lineCount - 1;
        int width = 0;
        for (int i = 1; i < lineCount; i++) {
            if (rawLines[i].length() > width) {
                width = rawLines[i].length();
            }
        }

        Map<Position, Entity> entities = new HashMap<>();
        Set<Position> destinations = new HashSet<>();
        Set<Integer> playerIds = new HashSet<>();
        Set<Integer> boxPlayerIds = new HashSet<>();

        for (int y = 0; y < height; y++) {
            String line = rawLines[y + 1];
            for (int x = 0; x < line.length(); x++) {
                char ch = line.charAt(x);
                Position pos = Position.of(x, y);

                switch (ch) {
                    case '#' -> entities.put(pos, new Wall());
                    case '.' -> entities.put(pos, new Empty());
                    case '@' -> {
                        entities.put(pos, new Empty());
                        destinations.add(pos);
                    }
                    default -> {
                        if (ch >= 'A' && ch <= 'Z') {
                            int playerId = ch - 'A';
                            entities.put(pos, new Player(playerId));
                            playerIds.add(playerId);
                        } else if (ch >= 'a' && ch <= 'z') {
                            int playerId = ch - 'a';
                            entities.put(pos, new Box(playerId));
                            boxPlayerIds.add(playerId);
                        }
                        // Any other character is ignored (treated as out-of-bounds)
                    }
                }
            }
            // Fill remaining cells in this row as Empty (for boundary checking)
            for (int x = line.length(); x < width; x++) {
                entities.put(Position.of(x, y), new Empty());
            }
        }

        // Validate closed boundary
        for (int x = 0; x < width; x++) {
            if (!(entities.get(Position.of(x, 0)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed at top edge");
            }
            if (!(entities.get(Position.of(x, height - 1)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed at bottom edge");
            }
        }
        for (int y = 0; y < height; y++) {
            if (!(entities.get(Position.of(0, y)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed at left edge");
            }
            if (!(entities.get(Position.of(width - 1, y)) instanceof Wall)) {
                throw new IllegalArgumentException("Map is not closed at right edge");
            }
        }

        // Validate at least one player
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException("Map must have at least one player");
        }

        // Validate number of destinations equals number of boxes
        if (destinations.size() != boxPlayerIds.size()) {
            throw new IllegalArgumentException(
                "Number of destinations (" + destinations.size()
                    + ") must equal number of boxes (" + boxPlayerIds.size() + ")"
            );
        }

        // Validate boxes reference valid player IDs
        for (int boxPlayerId : boxPlayerIds) {
            if (!playerIds.contains(boxPlayerId)) {
                throw new IllegalArgumentException(
                    "Box references player " + boxPlayerId + " but no such player exists"
                );
            }
        }

        return new GameMap(entities, destinations, undoLimit);
    }

    /**
     * Get the entity at the given position.
     *
     * @param position the position to query
     * @return the entity at that position, or null if out of bounds
     */
    public Entity getEntity(Position position) {
        return map.get(position);
    }

    /**
     * Place an entity at the given position. Only available during construction.
     *
     * @param position the position to place at
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
     * @return an Optional containing the undo limit, or empty if unlimited
     */
    public Optional<Integer> getUndoLimit() {
        if (undoLimit == -1) {
            return Optional.empty();
        }
        return Optional.of(undoLimit);
    }

    /**
     * @return the raw undo limit (-1 for unlimited, 0 for disabled)
     */
    public int getRawUndoLimit() {
        return undoLimit;
    }

    /**
     * @return the set of all player IDs in the map
     */
    public Set<Integer> getPlayerIds() {
        Set<Integer> ids = new HashSet<>();
        for (Entity entity : map.values()) {
            if (entity instanceof Player player) {
                ids.add(player.getId());
            }
        }
        return Collections.unmodifiableSet(ids);
    }

    /**
     * @return the internal map of positions to entities (unmodifiable)
     */
    public Map<Position, Entity> getMap() {
        return map;
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
