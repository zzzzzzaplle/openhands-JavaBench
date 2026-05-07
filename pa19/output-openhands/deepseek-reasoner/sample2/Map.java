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
    private final TerminationCell sourceCell;
    private final TerminationCell sinkCell;
    private final Set<Coordinate> filledTiles;
    int prevFilledTiles;
    private Integer prevFilledDistance;

    public Map(int rows, int cols, Cell[][] cells) {
        this.rows = rows;
        this.cols = cols;
        this.cells = cells;
        this.filledTiles = new HashSet<>();
        this.prevFilledTiles = 0;
        this.prevFilledDistance = null;

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

        // SOURCE must be in a non-edge cell (not on border)
        if (isEdgeCell(foundSource.coord)) {
            throw new IllegalArgumentException("SOURCE must be in a non-edge cell");
        }

        // SINK must be in an edge cell (on border)
        if (!isEdgeCell(foundSink.coord)) {
            throw new IllegalArgumentException("SINK must be in an edge cell");
        }

        // SOURCE must not point into a wall (the cell it points to must be a FillableCell)
        Coordinate sourceTarget = foundSource.coord.add(foundSource.pointingTo.getOffset());
        if (sourceTarget.row < 0 || sourceTarget.row >= rows || sourceTarget.col < 0 || sourceTarget.col >= cols
                || !(cells[sourceTarget.row][sourceTarget.col] instanceof FillableCell)) {
            throw new IllegalArgumentException("SOURCE must not point into a wall");
        }

        // SINK must point outside the map
        Coordinate sinkTarget = foundSink.coord.add(foundSink.pointingTo.getOffset());
        if (sinkTarget.row >= 0 && sinkTarget.row < rows && sinkTarget.col >= 0 && sinkTarget.col < cols) {
            throw new IllegalArgumentException("SINK must point outside the map");
        }

        this.sourceCell = foundSource;
        this.sinkCell = foundSink;
    }

    private boolean isEdgeCell(Coordinate coord) {
        return coord.row == 0 || coord.row == rows - 1 || coord.col == 0 || coord.col == cols - 1;
    }

    /**
     * Creates a Map from a string representation.
     */
    public static Map fromString(int rows, int cols, String cellsRep) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Map(rows, cols, cells);
    }

    /**
     * Gets the cell at the given row and column with bounds checking.
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return cells[row][col];
    }

    /**
     * Sets the cell at the given row and column with bounds checking.
     */
    public void setCell(int row, int col, Cell cell) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }
        cells[row][col] = cell;
    }

    /**
     * Tries to place a pipe at the given coordinates.
     *
     * @return false if coordinates are out of bounds, the target cell is not a FillableCell,
     * or the cell already contains a pipe.
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
     * Tries to place a pipe at the given coordinate.
     */
    public boolean tryPlacePipe(Coordinate coord, Pipe pipe) {
        return tryPlacePipe(coord.row, coord.col, pipe);
    }

    /**
     * Undoes a pipe placement at the given coordinate.
     */
    public void undo(Coordinate coord) {
        Cell cell = cells[coord.row][coord.col];
        if (cell instanceof FillableCell) {
            ((FillableCell) cell).setPipe(null);
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
     * Fills pipes within the specified distance from the source using BFS expansion.
     * Water propagates along pipes only when adjacent pipes have matching connection directions.
     */
    public void fillTiles(int distance) {
        if (prevFilledDistance != null && distance <= prevFilledDistance) {
            return;
        }

        // Track new tiles added in this round
        Set<Coordinate> newlyFilled = new HashSet<>();
        Set<Coordinate> visited = new HashSet<>(filledTiles);
        Queue<Coordinate> queue = new LinkedList<>(filledTiles);
        java.util.Map<Coordinate, Integer> distMap = new java.util.HashMap<>();
        for (Coordinate c : filledTiles) {
            distMap.put(c, 0);
        }

        while (!queue.isEmpty()) {
            Coordinate coord = queue.poll();
            int d = distMap.get(coord);

            if (d >= distance) {
                continue;
            }

            Cell cell = cells[coord.row][coord.col];
            Direction[] connections = getConnectionDirections(cell);

            if (connections == null || connections.length == 0) {
                continue;
            }

            for (Direction dir : connections) {
                Coordinate offset = dir.getOffset();
                Coordinate adjCoord = coord.add(offset);

                if (adjCoord.row < 0 || adjCoord.row >= rows || adjCoord.col < 0 || adjCoord.col >= cols) {
                    continue;
                }
                if (visited.contains(adjCoord)) {
                    continue;
                }

                Cell adjCell = cells[adjCoord.row][adjCoord.col];
                Direction opposite = dir.getOpposite();

                if (adjCell instanceof FillableCell) {
                    FillableCell afc = (FillableCell) adjCell;
                    if (afc.getPipe().isPresent()) {
                        Pipe adjPipe = afc.getPipe().get();
                        if (hasConnectingDirection(adjPipe, opposite)) {
                            visited.add(adjCoord);
                            distMap.put(adjCoord, d + 1);
                            queue.add(adjCoord);
                            if (!filledTiles.contains(adjCoord)) {
                                newlyFilled.add(adjCoord);
                            }
                        }
                    }
                } else if (adjCell instanceof TerminationCell) {
                    TerminationCell tc = (TerminationCell) adjCell;
                    if (tc.type == TerminationType.SINK && tc.pointingTo == opposite) {
                        visited.add(adjCoord);
                        distMap.put(adjCoord, d + 1);
                        queue.add(adjCoord);
                        if (!filledTiles.contains(adjCoord)) {
                            newlyFilled.add(adjCoord);
                        }
                    }
                }
            }
        }

        prevFilledTiles = newlyFilled.size();
        filledTiles.addAll(newlyFilled);
        prevFilledDistance = distance;

        // Mark newly filled pipes as filled
        for (Coordinate coord : newlyFilled) {
            Cell cell = cells[coord.row][coord.col];
            if (cell instanceof FillableCell) {
                FillableCell fc = (FillableCell) cell;
                fc.getPipe().ifPresent(p -> p.setFilled(true));
            } else if (cell instanceof TerminationCell) {
                ((TerminationCell) cell).setFilled();
            }
        }
    }

    private Direction[] getConnectionDirections(Cell cell) {
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            if (fc.getPipe().isPresent()) {
                return fc.getPipe().get().getConnections();
            }
            return null;
        }
        if (cell instanceof TerminationCell) {
            TerminationCell tc = (TerminationCell) cell;
            if (tc.isFilled && tc.type == TerminationType.SOURCE) {
                return new Direction[]{tc.pointingTo};
            }
            if (tc.isFilled && tc.type == TerminationType.SINK) {
                // Sink receives water, doesn't propagate further
                return new Direction[0];
            }
        }
        return null;
    }

    private boolean hasConnectingDirection(Pipe pipe, Direction targetDir) {
        for (Direction d : pipe.getConnections()) {
            if (d == targetDir) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses BFS to determine if a connected path exists from SOURCE to SINK through filled pipes.
     *
     * @return true if a path exists (win condition)
     */
    public boolean checkPath() {
        if (!filledTiles.contains(sourceCell.coord)) {
            return false;
        }

        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(sourceCell.coord);
        visited.add(sourceCell.coord);

        while (!queue.isEmpty()) {
            Coordinate coord = queue.poll();
            Cell cell = cells[coord.row][coord.col];

            // Check if we reached the sink
            if (cell == sinkCell) {
                return true;
            }

            Direction[] connections = getConnectionDirections(cell);
            if (connections == null) {
                continue;
            }

            for (Direction dir : connections) {
                Coordinate offset = dir.getOffset();
                Coordinate adjCoord = coord.add(offset);

                if (adjCoord.row < 0 || adjCoord.row >= rows || adjCoord.col < 0 || adjCoord.col >= cols) {
                    continue;
                }
                if (visited.contains(adjCoord)) {
                    continue;
                }

                Cell adjCell = cells[adjCoord.row][adjCoord.col];
                Direction opposite = dir.getOpposite();

                if (adjCell instanceof FillableCell) {
                    FillableCell afc = (FillableCell) adjCell;
                    if (afc.getPipe().isPresent()) {
                        Pipe adjPipe = afc.getPipe().get();
                        if (adjPipe.isFilled() && hasConnectingDirection(adjPipe, opposite)) {
                            visited.add(adjCoord);
                            queue.add(adjCoord);
                        }
                    }
                } else if (adjCell instanceof TerminationCell) {
                    TerminationCell tc = (TerminationCell) adjCell;
                    if (tc.isFilled() && tc.type == TerminationType.SINK && tc.pointingTo == opposite) {
                        visited.add(adjCoord);
                        queue.add(adjCoord);
                    }
                }
            }
        }

        return visited.contains(sinkCell.coord);
    }

    /**
     * Returns true when prevFilledTiles == 0 (no new pipes were filled in the previous round).
     */
    public boolean hasLost() {
        return prevFilledTiles == 0 && prevFilledDistance != null && prevFilledDistance > 0;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public TerminationCell getSourceCell() {
        return sourceCell;
    }

    public TerminationCell getSinkCell() {
        return sinkCell;
    }

    /**
     * Displays the map.
     */
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
