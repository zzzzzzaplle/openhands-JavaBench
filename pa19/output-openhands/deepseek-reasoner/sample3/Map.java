import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * The game map containing cells, managing pipe placement and water flow.
 */
public class Map {
    private final int rows;
    private final int cols;
    public final Cell[][] cells;
    private TerminationCell sourceCell;
    private TerminationCell sinkCell;
    private final Set<Coordinate> filledTiles = new HashSet<>();
    private int prevFilledTiles;
    private Integer prevFilledDistance;

    public Map(int rows, int cols, Cell[][] cells) {
        this.rows = rows;
        this.cols = cols;
        this.cells = cells;
        validateAndLocateTerminals();
    }

    /**
     * Creates a Map from a string representation.
     */
    public static Map fromString(int rows, int cols, String cellsRep) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Map(rows, cols, cells);
    }

    private void validateAndLocateTerminals() {
        TerminationCell foundSource = null;
        TerminationCell foundSink = null;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                if (cell instanceof TerminationCell) {
                    TerminationCell tc = (TerminationCell) cell;
                    if (tc.type == TerminationType.SOURCE) {
                        if (foundSource != null) {
                            throw new IllegalArgumentException("Map must contain exactly one SOURCE");
                        }
                        foundSource = tc;
                    } else if (tc.type == TerminationType.SINK) {
                        if (foundSink != null) {
                            throw new IllegalArgumentException("Map must contain exactly one SINK");
                        }
                        foundSink = tc;
                    }
                }
            }
        }

        if (foundSource == null) {
            throw new IllegalArgumentException("Map must contain exactly one SOURCE");
        }
        if (foundSink == null) {
            throw new IllegalArgumentException("Map must contain exactly one SINK");
        }

        // Validate SOURCE is not on edge
        Coordinate sc = foundSource.coord;
        if (sc.row == 0 || sc.row == rows - 1 || sc.col == 0 || sc.col == cols - 1) {
            throw new IllegalArgumentException("SOURCE must be in a non-edge cell");
        }

        // Validate SOURCE does not point into a wall
        Coordinate sourceTarget = sc.add(foundSource.pointingTo.getOffset());
        if (sourceTarget.row < 0 || sourceTarget.row >= rows
                || sourceTarget.col < 0 || sourceTarget.col >= cols
                || cells[sourceTarget.row][sourceTarget.col] instanceof Wall) {
            throw new IllegalArgumentException("SOURCE must not point into a wall");
        }

        // Validate SINK is on edge
        Coordinate sinkCoord = foundSink.coord;
        boolean onEdge = sinkCoord.row == 0 || sinkCoord.row == rows - 1
                || sinkCoord.col == 0 || sinkCoord.col == cols - 1;
        if (!onEdge) {
            throw new IllegalArgumentException("SINK must be in an edge cell");
        }

        // Validate SINK points outside the map
        Coordinate sinkTarget = sinkCoord.add(foundSink.pointingTo.getOffset());
        if (!(sinkTarget.row < 0 || sinkTarget.row >= rows
                || sinkTarget.col < 0 || sinkTarget.col >= cols)) {
            throw new IllegalArgumentException("SINK must point outside the map");
        }

        this.sourceCell = foundSource;
        this.sinkCell = foundSink;
    }

    public TerminationCell getSourceCell() {
        return sourceCell;
    }

    public TerminationCell getSinkCell() {
        return sinkCell;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return cells[row][col];
    }

    public boolean setCell(int row, int col, Cell cell) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        cells[row][col] = cell;
        return true;
    }

    /**
     * Attempts to place a pipe at the given coordinate.
     */
    public boolean tryPlacePipe(final Coordinate coord, final Pipe pipe) {
        return tryPlacePipe(coord.row, coord.col, pipe);
    }

    /**
     * Attempts to place a pipe at the given row and column.
     */
    public boolean tryPlacePipe(int row, int col, Pipe p) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        Cell cell = cells[row][col];
        if (!(cell instanceof FillableCell)) {
            return false;
        }
        FillableCell fc = (FillableCell) cell;
        if (fc.getPipe().isPresent()) {
            return false;
        }
        fc.setPipe(p);
        return true;
    }

    /**
     * Removes the pipe from the cell at the given coordinate.
     */
    public void undo(Coordinate coord) {
        Cell cell = getCell(coord.row, coord.col);
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            fc.setPipe(null);
            fc.setFilled(false);
            filledTiles.remove(coord);
        }
    }

    /**
     * Marks the source cell as filled.
     */
    public void fillBeginTile() {
        sourceCell.setFilled();
        filledTiles.add(sourceCell.coord);
    }

    /**
     * Fills pipes within the specified distance from source using BFS.
     */
    public void fillTiles(int distance) {
        if (distance <= 0) {
            prevFilledTiles = 0;
            return;
        }

        if (prevFilledDistance != null && distance <= prevFilledDistance) {
            // Already processed this distance, nothing new to fill
            prevFilledTiles = 0;
            return;
        }

        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        Queue<Integer> distQueue = new LinkedList<>();

        visited.add(sourceCell.coord);
        queue.add(sourceCell.coord);
        distQueue.add(0);

        int newFilled = 0;

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            int currentDist = distQueue.poll();

            if (currentDist >= distance) {
                continue;
            }

            Cell cell = getCell(current.row, current.col);
            if (cell == null) {
                continue;
            }

            // Get connection directions from the current cell
            Direction[] connections = getConnections(cell, current.equals(sourceCell.coord));

            if (connections == null) {
                continue;
            }

            for (Direction dir : connections) {
                Coordinate next = current.add(dir.getOffset());
                if (next.row < 0 || next.row >= rows || next.col < 0 || next.col >= cols) {
                    continue;
                }
                if (visited.contains(next)) {
                    continue;
                }
                visited.add(next);

                Cell nextCell = getCell(next.row, next.col);
                if (nextCell == null) {
                    continue;
                }

                // Check if next cell accepts flow from this direction
                Direction[] nextConns = getAcceptingDirections(nextCell);
                if (nextConns == null) {
                    continue;
                }

                boolean matches = false;
                for (Direction nd : nextConns) {
                    if (nd == dir.getOpposite()) {
                        matches = true;
                        break;
                    }
                }
                if (!matches) {
                    continue;
                }

                if (nextCell instanceof TerminationCell) {
                    TerminationCell tc = (TerminationCell) nextCell;
                    if (!tc.isFilled()) {
                        tc.setFilled();
                        filledTiles.add(next);
                        newFilled++;
                    }
                    continue;
                }

                if (nextCell instanceof FillableCell) {
                    FillableCell fc = (FillableCell) nextCell;
                    if (fc.getPipe().isPresent()) {
                        if (!fc.isFilled()) {
                            fc.setFilled(true);
                            filledTiles.add(next);
                            newFilled++;
                        }
                        queue.add(next);
                        distQueue.add(currentDist + 1);
                    }
                }
            }
        }

        prevFilledTiles = newFilled;
        prevFilledDistance = distance;
    }

    /**
     * Returns the connection directions for a cell, used for water flow expansion.
     */
    private Direction[] getConnections(Cell cell, boolean isSourceCoord) {
        if (cell instanceof TerminationCell) {
            TerminationCell tc = (TerminationCell) cell;
            if (tc.isFilled()) {
                return new Direction[]{tc.pointingTo};
            }
            return null;
        }
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            if (fc.getPipe().isPresent() && fc.isFilled()) {
                return fc.getPipe().get().getConnections();
            }
            return null;
        }
        return null;
    }

    /**
     * Returns the directions from which a cell can accept flow (connections reversed).
     */
    private Direction[] getAcceptingDirections(Cell cell) {
        if (cell instanceof TerminationCell) {
            // A sink accepts flow from the opposite of its pointingTo direction
            TerminationCell tc = (TerminationCell) cell;
            return new Direction[]{tc.pointingTo.getOpposite()};
        }
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            if (fc.getPipe().isPresent()) {
                return fc.getPipe().get().getConnections();
            }
            return null;
        }
        return null;
    }

    /**
     * Uses BFS to determine if a connected path exists from SOURCE to SINK
     * through filled pipes.
     */
    public boolean checkPath() {
        if (!sourceCell.isFilled()) {
            return false;
        }

        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();

        visited.add(sourceCell.coord);
        queue.add(sourceCell.coord);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            // Check if we reached the sink
            if (current.equals(sinkCell.coord)) {
                return true;
            }

            Cell cell = getCell(current.row, current.col);
            if (cell == null) {
                continue;
            }

            Direction[] connections = getConnections(cell, current.equals(sourceCell.coord));
            if (connections == null) {
                continue;
            }

            for (Direction dir : connections) {
                Coordinate next = current.add(dir.getOffset());
                if (next.row < 0 || next.row >= rows || next.col < 0 || next.col >= cols) {
                    continue;
                }
                if (visited.contains(next)) {
                    continue;
                }

                Cell nextCell = getCell(next.row, next.col);
                if (nextCell == null) {
                    continue;
                }

                // Check matching connection
                Direction[] nextConns = getAcceptingDirections(nextCell);
                if (nextConns == null) {
                    continue;
                }

                boolean matches = false;
                for (Direction nd : nextConns) {
                    if (nd == dir.getOpposite()) {
                        matches = true;
                        break;
                    }
                }
                if (!matches) {
                    continue;
                }

                visited.add(next);

                // Add to queue if it's a filled pipe or the sink
                if (nextCell instanceof FillableCell) {
                    FillableCell fc = (FillableCell) nextCell;
                    if (fc.getPipe().isPresent() && fc.isFilled()) {
                        queue.add(next);
                    }
                } else if (nextCell instanceof TerminationCell) {
                    queue.add(next);
                }
            }
        }

        return false;
    }

    /**
     * Returns true when no new pipes were filled in the previous round (loss condition).
     */
    public boolean hasLost() {
        return prevFilledTiles == 0;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void display() {
        final int padLength = Integer.valueOf(rows - 1).toString().length();

        Runnable printColumns = () -> {
            System.out.print(StringUtils.createPadding(padLength, ' '));
            System.out.print(' ');
            for (int i = 0; i < cols - 2; ++i) {
                System.out.print((char) ('A' + i));
            }
            System.out.println();
        };

        printColumns.run();

        for (int i = 0; i < rows; ++i) {
            if (i != 0 && i != rows - 1) {
                System.out.print(String.format("%1$" + padLength + "s", i));
            } else {
                System.out.print(StringUtils.createPadding(padLength, ' '));
            }

            Arrays.stream(cells[i]).forEachOrdered(elem -> System.out.print(elem.toSingleChar()));

            if (i != 0 && i != rows - 1) {
                System.out.print(i);
            }

            System.out.println();
        }

        printColumns.run();
    }
}
