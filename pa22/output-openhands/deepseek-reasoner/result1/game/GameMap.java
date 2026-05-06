package assignment.game;

import java.util.*;

/**
 * Represents the static game board layout parsed from a map file.
 * Stores the initial entity positions, destinations, and the undo limit.
 */
public class GameMap {
    private final Map<Position, Entity> map;
    private final int maxWidth;
    private final int maxHeight;
    private final Set<Position> destinations;
    private final int undoLimit;

    /**
     * Create a GameMap with the given dimensions, destinations, and undo limit.
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
     * Parse a map text representation into a GameMap.
     * <p>
     * The first line of the text is the undo limit. Subsequent lines represent the grid:
     * '#' for walls, '.' for empty spaces, '@' for destinations,
     * uppercase letters (A-Z) for players, lowercase letters (a-z) for boxes.
     *
     * @param mapText the text representation of the map
     * @return the parsed GameMap
     * @throws IllegalArgumentException if the map is invalid
     */
    public static GameMap parse(String mapText) {
        String[] lines = mapText.replace("\r\n", "\n").split("\n");

        // Find first non-empty line for undo limit
        int lineIdx = 0;
        while (lineIdx < lines.length && lines[lineIdx].trim().isEmpty()) {
            lineIdx++;
        }
        if (lineIdx >= lines.length) {
            throw new IllegalArgumentException("Map text is empty");
        }

        int undoLimit;
        try {
            undoLimit = Integer.parseInt(lines[lineIdx].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid undo limit: " + lines[lineIdx].trim());
        }

        // Collect map rows
        List<String> rows = new ArrayList<>();
        for (int i = lineIdx + 1; i < lines.length; i++) {
            String row = lines[i];
            if (!row.isEmpty()) {
                rows.add(row);
            }
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No map rows found");
        }

        int height = rows.size();
        int width = rows.get(0).length();

        // Validate all rows have the same width
        for (String row : rows) {
            if (row.length() != width) {
                throw new IllegalArgumentException("Map rows have inconsistent widths");
            }
        }

        // Validate closed boundary (all edge cells must be walls)
        for (int x = 0; x < width; x++) {
            if (rows.get(0).charAt(x) != '#') {
                throw new IllegalArgumentException("Top boundary is not closed");
            }
            if (rows.get(height - 1).charAt(x) != '#') {
                throw new IllegalArgumentException("Bottom boundary is not closed");
            }
        }
        for (int y = 0; y < height; y++) {
            if (rows.get(y).charAt(0) != '#') {
                throw new IllegalArgumentException("Left boundary is not closed");
            }
            if (rows.get(y).charAt(width - 1) != '#') {
                throw new IllegalArgumentException("Right boundary is not closed");
            }
        }

        // Parse entities and destinations
        Map<Position, Entity> map = new HashMap<>();
        Set<Position> destinations = new HashSet<>();
        Set<Integer> playerIds = new HashSet<>();
        Set<Integer> boxPlayerIds = new HashSet<>();

        for (int y = 0; y < height; y++) {
            String row = rows.get(y);
            for (int x = 0; x < width; x++) {
                char c = row.charAt(x);
                Position pos = Position.of(x, y);

                switch (c) {
                    case '#':
                        map.put(pos, new Wall());
                        break;
                    case '.':
                        map.put(pos, new Empty());
                        break;
                    case '@':
                        map.put(pos, new Empty());
                        destinations.add(pos);
                        break;
                    default:
                        if (c >= 'A' && c <= 'Z') {
                            int playerId = c - 'A';
                            map.put(pos, new Player(playerId));
                            playerIds.add(playerId);
                        } else if (c >= 'a' && c <= 'z') {
                            int boxPlayerId = c - 'a';
                            map.put(pos, new Box(boxPlayerId));
                            boxPlayerIds.add(boxPlayerId);
                        } else {
                            throw new IllegalArgumentException("Unexpected character '" + c + "' at position " + pos);
                        }
                        break;
                }
            }
        }

        // Validate map rules
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException("Map must have at least one player");
        }

        if (destinations.size() != boxPlayerIds.size()) {
            throw new IllegalArgumentException(
                "Number of destinations (" + destinations.size() + ") must equal number of boxes (" + boxPlayerIds.size() + ")"
            );
        }

        for (int boxPlayerId : boxPlayerIds) {
            if (!playerIds.contains(boxPlayerId)) {
                throw new IllegalArgumentException("Box references player " + boxPlayerId + " which does not exist in the map");
            }
        }

        return new GameMap(map, destinations, undoLimit);
    }

    /**
     * Get the entity at the given position.
     *
     * @param position the position to query
     * @return the entity at the position, or null if none
     */
    public Entity getEntity(Position position) {
        return map.get(position);
    }

    /**
     * Place an entity at the given position.
     *
     * @param position the position to place at
     * @param entity   the entity to place
     */
    public void putEntity(Position position, Entity entity) {
        map.put(position, entity);
    }

    /**
     * @return the set of destination positions
     */
    public Set<Position> getDestinations() {
        return destinations;
    }

    /**
     * Get the undo limit.
     *
     * @return an Optional containing the undo limit if set, or empty if unlimited
     */
    public Optional<Integer> getUndoLimit() {
        if (undoLimit >= 0) {
            return Optional.of(undoLimit);
        }
        return Optional.empty();
    }

    /**
     * @return the raw undo limit value
     */
    public int getRawUndoLimit() {
        return undoLimit;
    }

    /**
     * @return the set of all player IDs present on the map
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

    /**
     * @return the internal map of positions to entities (unmodifiable)
     */
    Map<Position, Entity> getEntities() {
        return map;
    }
}
