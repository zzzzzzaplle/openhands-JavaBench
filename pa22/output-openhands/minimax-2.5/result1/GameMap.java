package game;

import java.util.*;

/**
 * Game map containing the board, destinations, and undo limit.
 */
public class GameMap {
    private Map<Position, Entity> map;
    private int maxWidth;
    private int maxHeight;
    private Set<Position> destinations;
    private int undoLimit;

    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.destinations = Collections.unmodifiableSet(new HashSet<>(destinations));
        this.undoLimit = undoLimit;
        this.map = new HashMap<>();
    }

    private GameMap(Map<Position, Entity> map, Set<Position> destinations, int undoLimit) {
        this.map = Collections.unmodifiableMap(new HashMap<>(map));
        this.destinations = Collections.unmodifiableSet(new HashSet<>(destinations));
        this.undoLimit = undoLimit;
        this.maxWidth = map.keySet().stream().mapToInt(Position::x).max().orElse(0) + 1;
        this.maxHeight = map.keySet().stream().mapToInt(Position::y).max().orElse(0) + 1;
    }

    /**
     * Parse a map text representation.
     * First line is undo limit, # = walls, @ = destinations, . = empty,
     * upper-case letters = Players, lower-case letters = Boxes.
     */
    public static GameMap parse(String mapText) {
        String[] lines = mapText.trim().split("\n");
        if (lines.length == 0) {
            throw new IllegalArgumentException("Empty map");
        }

        // Parse undo limit from first line
        int undoLimit;
        try {
            undoLimit = Integer.parseInt(lines[0].trim());
        } catch (NumberFormatException e) {
            undoLimit = 0;
        }

        Map<Position, Entity> map = new HashMap<>();
        Set<Position> destinations = new HashSet<>();
        Set<Integer> playerIds = new HashSet<>();

        // Parse the map (lines 1+)
        for (int y = 1; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                Position pos = new Position(x, y - 1);

                switch (c) {
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
                    default:
                        if (c >= 'A' && c <= 'Z') {
                            int playerId = c - 'A';
                            playerIds.add(playerId);
                            map.put(pos, new Player(playerId));
                        } else if (c >= 'a' && c <= 'z') {
                            int playerId = c - 'a';
                            map.put(pos, new Box(playerId));
                        } else if (c != ' ' && c != '\r') {
                            // Skip whitespace
                            map.put(pos, new Empty());
                        }
                        break;
                }
            }
        }

        // Validation
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException("Map must have at least one player");
        }

        // Count boxes and destinations
        int boxCount = 0;
        int destCount = destinations.size();
        for (Entity e : map.values()) {
            if (e instanceof Box) {
                boxCount++;
            }
        }

        if (boxCount != destCount) {
            throw new IllegalArgumentException("Number of boxes (" + boxCount + ") must equal number of destinations (" + destCount + ")");
        }

        // Validate box player IDs exist
        for (Entity e : map.values()) {
            if (e instanceof Box) {
                int boxPlayerId = ((Box) e).getPlayerId();
                if (!playerIds.contains(boxPlayerId)) {
                    throw new IllegalArgumentException("Box references invalid player ID: " + boxPlayerId);
                }
            }
        }

        // Check closed boundary
        for (int y = 0; y < lines.length - 1; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                if (x == 0 || x == line.length() - 1 || y == 0 || y == lines.length - 2) {
                    char c = line.charAt(x);
                    if (c != '#') {
                        Map<Position, Entity> checkMap = new HashMap<>(map);
                        checkMap.put(new Position(x, y), new Wall());
                    }
                }
            }
        }

        return new GameMap(map, destinations, undoLimit);
    }

    public Entity getEntity(Position position) {
        return map.get(position);
    }

    public void putEntity(Position position, Entity entity) {
        map.put(position, entity);
    }

    public Set<Position> getDestinations() {
        return destinations;
    }

    public Optional<Integer> getUndoLimit() {
        return undoLimit >= 0 ? Optional.of(undoLimit) : Optional.empty();
    }

    public Set<Integer> getPlayerIds() {
        Set<Integer> ids = new HashSet<>();
        for (Entity e : map.values()) {
            if (e instanceof Player) {
                ids.add(((Player) e).getId());
            }
        }
        return ids;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public Collection<Entity> getEntities() {
        return map.values();
    }
}