package assignment.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the static layout of a Sokoban game map.
 * Stores the grid of entities, destination positions, and undo limit.
 * Performs validation when parsing a map from its text representation.
 */
public class GameMap {

    private final Map<Position, Entity> map;
    private final int maxWidth;
    private final int maxHeight;
    private final Set<Position> destinations;
    private final int undoLimit;

    /**
     * Creates a mutable GameMap for programmatic construction.
     *
     * @param maxWidth     the width of the map grid
     * @param maxHeight    the height of the map grid
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
     * Parses a text representation of a Sokoban map into a GameMap.
     * <p>
     * The first line specifies the undo limit. Subsequent lines define the grid:
     * '#' = wall, '@' = destination, '.' = empty, A-Z = players, a-z = boxes.
     * </p>
     * <p>
     * Validation rules:
     * <ul>
     *   <li>The map must have a closed boundary (walls around the perimeter)</li>
     *   <li>There must be at least one player</li>
     *   <li>The number of destinations must equal the number of boxes</li>
     *   <li>Each box must reference a valid player ID that exists on the map</li>
     * </ul>
     *
     * @param mapText the text representation of the map
     * @return the parsed GameMap
     * @throws IllegalArgumentException if the map is invalid
     */
    public static GameMap parse(String mapText) throws IllegalArgumentException {
        String[] lines = mapText.split("\n");

        // First line is the undo limit
        int undoLimit = Integer.parseInt(lines[0].trim());

        // Remaining lines form the grid
        String[] gridLines = new String[lines.length - 1];
        System.arraycopy(lines, 1, gridLines, 0, lines.length - 1);

        int height = gridLines.length;
        int width = 0;
        for (String line : gridLines) {
            if (line.length() > width) {
                width = line.length();
            }
        }

        Map<Position, Entity> entities = new HashMap<>();
        Set<Position> destinations = new HashSet<>();
        Set<Integer> playerIds = new HashSet<>();
        Map<Integer, Integer> boxCountByPlayer = new HashMap<>();

        // Parse the grid
        for (int y = 0; y < height; y++) {
            String line = gridLines[y];
            for (int x = 0; x < line.length(); x++) {
                char ch = line.charAt(x);
                Position pos = Position.of(x, y);

                if (ch == '#') {
                    entities.put(pos, new Wall());
                } else if (ch == '.') {
                    entities.put(pos, new Empty());
                } else if (ch == '@') {
                    entities.put(pos, new Empty());
                    destinations.add(pos);
                } else if (ch >= 'A' && ch <= 'Z') {
                    int playerId = ch - 'A';
                    entities.put(pos, new Player(playerId));
                    playerIds.add(playerId);
                } else if (ch >= 'a' && ch <= 'z') {
                    int playerId = ch - 'a';
                    entities.put(pos, new Box(playerId));
                    boxCountByPlayer.merge(playerId, 1, Integer::sum);
                }
                // Spaces are left unmapped (treated as empty)
            }
        }

        // Validation: closed boundary
        validateClosedBoundary(entities, width, height);

        // Validation: at least one player
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException("Map must have at least one player.");
        }

        // Validation: number of destinations must equal number of boxes
        long totalBoxes = boxCountByPlayer.values().stream().mapToInt(Integer::intValue).sum();
        if (destinations.size() != totalBoxes) {
            throw new IllegalArgumentException(
                "Number of destinations (" + destinations.size()
                    + ") must equal number of boxes (" + totalBoxes + ")."
            );
        }

        // Validation: each box must reference a valid player ID
        for (int boxPlayerId : boxCountByPlayer.keySet()) {
            if (!playerIds.contains(boxPlayerId)) {
                throw new IllegalArgumentException(
                    "Box references player ID " + boxPlayerId + " which does not exist on the map."
                );
            }
        }

        return new GameMap(entities, destinations, undoLimit);
    }

    /**
     * Validates that the map has a closed boundary of walls.
     */
    private static void validateClosedBoundary(Map<Position, Entity> entities, int width, int height) {
        for (int x = 0; x < width; x++) {
            // Top row
            Position topPos = Position.of(x, 0);
            Entity topEntity = entities.get(topPos);
            if (!(topEntity instanceof Wall)) {
                throw new IllegalArgumentException("Map must have a closed boundary (wall at top edge position " + topPos + ").");
            }
            // Bottom row
            Position bottomPos = Position.of(x, height - 1);
            Entity bottomEntity = entities.get(bottomPos);
            if (!(bottomEntity instanceof Wall)) {
                throw new IllegalArgumentException("Map must have a closed boundary (wall at bottom edge position " + bottomPos + ").");
            }
        }
        for (int y = 0; y < height; y++) {
            // Left column
            Position leftPos = Position.of(0, y);
            Entity leftEntity = entities.get(leftPos);
            if (!(leftEntity instanceof Wall)) {
                throw new IllegalArgumentException("Map must have a closed boundary (wall at left edge position " + leftPos + ").");
            }
            // Right column
            Position rightPos = Position.of(width - 1, y);
            Entity rightEntity = entities.get(rightPos);
            if (!(rightEntity instanceof Wall)) {
                throw new IllegalArgumentException("Map must have a closed boundary (wall at right edge position " + rightPos + ").");
            }
        }
    }

    /**
     * Returns the entity at the given position, or null if the position is unoccupied.
     *
     * @param position the position to query
     * @return the entity at the position, or null
     */
    public Entity getEntity(Position position) {
        return map.get(position);
    }

    /**
     * Places an entity at the given position.
     *
     * @param position the position to place the entity
     * @param entity   the entity to place
     */
    public void putEntity(Position position, Entity entity) {
        map.put(position, entity);
    }

    /**
     * Returns the set of destination positions.
     *
     * @return unmodifiable set of destinations
     */
    public Set<Position> getDestinations() {
        return destinations;
    }

    /**
     * Returns the undo limit, if finite.
     *
     * @return an Optional containing the undo limit if finite, or empty if unlimited
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
        return map.values().stream()
            .filter(e -> e instanceof Player)
            .map(e -> ((Player) e).getId())
            .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the maximum width of the map.
     *
     * @return the width in columns
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Returns the maximum height of the map.
     *
     * @return the height in rows
     */
    public int getMaxHeight() {
        return maxHeight;
    }
}
