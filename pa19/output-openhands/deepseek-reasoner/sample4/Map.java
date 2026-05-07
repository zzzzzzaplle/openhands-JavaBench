import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * The game map holding a 2-D grid of cells.
 */
public class Map {

    private final int rows;
    private final int cols;
    final Cell[][] cells; // package-visible for display helpers

    private final TerminationCell sourceCell;
    private final TerminationCell sinkCell;
    private final Set<Coordinate> filledTiles;
    int prevFilledTiles;
    Integer prevFilledDistance;

    /**
     * Constructs a map with the given dimensions and cells.
     * Validates the required constraints:
     * <ul>
     *   <li>exactly one SOURCE in a non-edge cell</li>
     *   <li>exactly one SINK in an edge cell</li>
     *   <li>SOURCE must not point into a wall</li>
     *   <li>SINK must point outside the map</li>
     * </ul>
     *
     * @param rows  number of rows
     * @param cols  number of columns
     * @param cells the cell grid
     * @throws IllegalArgumentException if constraints are violated
     */
    Map(int rows, int cols, Cell[][] cells) {
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
                if (cells[r][c] instanceof TerminationCell) {
                    TerminationCell tc = (TerminationCell) cells[r][c];
                    if (tc.type == TerminationType.SOURCE) {
                        if (foundSource != null) {
                            throw new IllegalArgumentException("Multiple SOURCE cells found");
                        }
                        foundSource = tc;
                    } else {
                        if (foundSink != null) {
                            throw new IllegalArgumentException("Multiple SINK cells found");
                        }
                        foundSink = tc;
                    }
                }
            }
        }

        if (foundSource == null) {
            throw new IllegalArgumentException("No SOURCE cell found");
        }
        if (foundSink == null) {
            throw new IllegalArgumentException("No SINK cell found");
        }

        // Source must be in a non-edge cell
        Coordinate sc = foundSource.coord;
        if (sc.row == 0 || sc.row == rows - 1 || sc.col == 0 || sc.col == cols - 1) {
            throw new IllegalArgumentException("SOURCE must be in a non-edge cell");
        }

        // Sink must be in an edge cell
        Coordinate sk = foundSink.coord;
        if (sk.row != 0 && sk.row != rows - 1 && sk.col != 0 && sk.col != cols - 1) {
            throw new IllegalArgumentException("SINK must be in an edge cell");
        }

        // SOURCE must not point into a wall
        Coordinate sourceOffset = sc.add(foundSource.pointingTo.getOffset());
        if (sourceOffset.row < 0 || sourceOffset.row >= rows || sourceOffset.col < 0 || sourceOffset.col >= cols
                || cells[sourceOffset.row][sourceOffset.col] instanceof Wall) {
            throw new IllegalArgumentException("SOURCE must not point into a wall");
        }

        // SINK must point outside the map
        Coordinate sinkOffset = sk.add(foundSink.pointingTo.getOffset());
        if (sinkOffset.row >= 0 && sinkOffset.row < rows && sinkOffset.col >= 0 && sinkOffset.col < cols) {
            throw new IllegalArgumentException("SINK must point outside the map");
        }

        this.sourceCell = foundSource;
        this.sinkCell = foundSink;
    }

    /**
     * Creates a map from a string representation.
     *
     * @param rows    number of rows
     * @param cols    number of columns
     * @param cellsRep multiline string of cell characters
     * @return a new Map
     */
    static Map fromString(int rows, int cols, String cellsRep) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Map(rows, cols, cells);
    }

    /**
     * Returns the cell at the given row and column.
     *
     * @param row row index
     * @param col column index
     * @return the cell, or null if out of bounds
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return cells[row][col];
    }

    /**
     * Sets the cell at the given row and column.
     *
     * @param row  row index
     * @param col  column index
     * @param cell the cell to place
     */
    public void setCell(int row, int col, Cell cell) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }
        cells[row][col] = cell;
    }

    /**
     * Tries to place a pipe at the given coordinate.
     *
     * @param coord target coordinate
     * @param pipe  the pipe to place
     * @return true if placement succeeded
     */
    public boolean tryPlacePipe(final Coordinate coord, final Pipe pipe) {
        return tryPlacePipe(coord.row, coord.col, pipe);
    }

    /**
     * Tries to place a pipe at the given row and column.
     *
     * @param row  row index
     * @param col  column index
     * @param p    the pipe to place
     * @return true if placement succeeded
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
     * Removes the pipe from a fillable cell (undo).
     *
     * @param coord coordinate to clear
     */
    public void undo(Coordinate coord) {
        Cell cell = cells[coord.row][coord.col];
        if (cell instanceof FillableCell) {
            ((FillableCell) cell).clearPipe();
        }
    }

    /**
     * Marks the source cell as filled with water.
     */
    public void fillBeginTile() {
        sourceCell.setFilled();
    }

    /**
     * Gets the connection directions for a cell (pipe connections or termination direction).
     */
    private Set<Direction> getConnectionDirections(Cell cell) {
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            if (fc.getPipe().isPresent()) {
                Set<Direction> dirs = new HashSet<>();
                Collections.addAll(dirs, fc.getPipe().get().getConnections());
                return dirs;
            }
            return Collections.emptySet();
        } else if (cell instanceof TerminationCell) {
            TerminationCell tc = (TerminationCell) cell;
            return Collections.singleton(tc.pointingTo);
        }
        return Collections.emptySet();
    }

    /**
     * Checks whether water can flow from one cell to an adjacent cell in the given direction.
     */
    private boolean canFlowBetween(Cell from, Cell to, Direction dir) {
        Set<Direction> toDirs = getConnectionDirections(to);
        return toDirs.contains(dir.getOpposite());
    }

    /**
     * Fills pipes within the specified distance from source using BFS-style expansion.
     *
     * @param targetDistance the maximum BFS distance from source
     */
    public void fillTiles(int targetDistance) {
        if (targetDistance <= 0) {
            return;
        }

        // BFS from source
        Queue<Coordinate> queue = new LinkedList<>();
        java.util.Map<Coordinate, Integer> distances = new HashMap<>();

        queue.add(sourceCell.coord);
        distances.put(sourceCell.coord, 0);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            int currDist = distances.get(current);

            if (currDist >= targetDistance) {
                continue;
            }

            Cell currentCell = cells[current.row][current.col];
            Set<Direction> outgoingDirs = getConnectionDirections(currentCell);

            for (Direction dir : outgoingDirs) {
                Coordinate neighbor = current.add(dir.getOffset());
                if (neighbor.row < 0 || neighbor.row >= rows || neighbor.col < 0 || neighbor.col >= cols) {
                    continue;
                }
                if (distances.containsKey(neighbor)) {
                    continue;
                }

                Cell neighborCell = cells[neighbor.row][neighbor.col];

                // Water can only flow through fillable cells that have pipes, or termination cells
                boolean validNeighbor = false;
                if (neighborCell instanceof FillableCell) {
                    validNeighbor = ((FillableCell) neighborCell).getPipe().isPresent();
                } else if (neighborCell instanceof TerminationCell) {
                    validNeighbor = true;
                }

                if (!validNeighbor) {
                    continue;
                }

                if (canFlowBetween(currentCell, neighborCell, dir)) {
                    distances.put(neighbor, currDist + 1);
                    queue.add(neighbor);
                }
            }
        }

        // Fill cells up to the target distance
        int newFilled = 0;
        for (java.util.Map.Entry<Coordinate, Integer> entry : distances.entrySet()) {
            Coordinate coord = entry.getKey();
            int dist = entry.getValue();
            if (dist <= targetDistance && dist > 0) {
                Cell cell = cells[coord.row][coord.col];
                if (!filledTiles.contains(coord)) {
                    if (cell instanceof FillableCell) {
                        ((FillableCell) cell).isFilled = true;
                        filledTiles.add(coord);
                        newFilled++;
                    } else if (cell instanceof TerminationCell) {
                        ((TerminationCell) cell).setFilled();
                        filledTiles.add(coord);
                        newFilled++;
                    }
                }
            }
        }

        prevFilledTiles = newFilled;
        prevFilledDistance = targetDistance;
    }

    /**
     * Checks whether a connected path exists from SOURCE to SINK through filled pipes (win condition).
     *
     * @return true if a complete path exists
     */
    public boolean checkPath() {
        // BFS from source to sink through filled tiles
        if (!filledTiles.contains(sinkCell.coord)) {
            return false;
        }

        Queue<Coordinate> queue = new LinkedList<>();
        Set<Coordinate> visited = new HashSet<>();

        queue.add(sourceCell.coord);
        visited.add(sourceCell.coord);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            if (current.equals(sinkCell.coord)) {
                return true;
            }

            Cell currentCell = cells[current.row][current.col];
            Set<Direction> dirs = getConnectionDirections(currentCell);

            for (Direction dir : dirs) {
                Coordinate neighbor = current.add(dir.getOffset());
                if (neighbor.row < 0 || neighbor.row >= rows || neighbor.col < 0 || neighbor.col >= cols) {
                    continue;
                }
                if (visited.contains(neighbor)) {
                    continue;
                }

                // Only traverse through filled tiles
                if (!filledTiles.contains(neighbor) && !neighbor.equals(sourceCell.coord)) {
                    continue;
                }

                Cell neighborCell = cells[neighbor.row][neighbor.col];
                if (canFlowBetween(currentCell, neighborCell, dir)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return false;
    }

    /**
     * Returns true when no new tiles were filled in the previous round (loss condition).
     *
     * @return true if the player has lost
     */
    public boolean hasLost() {
        return prevFilledTiles == 0;
    }

    /**
     * Displays the map with row and column labels.
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
