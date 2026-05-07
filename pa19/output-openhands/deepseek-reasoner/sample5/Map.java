import java.util.*;

/**
 * The game map holding all cells and handling pipe placement, water flow,
 * and win/loss detection.
 */
public class Map {

    private final int rows;
    private final int cols;
    final Cell[][] cells;
    private TerminationCell sourceCell;
    private TerminationCell sinkCell;
    private final Set<Coordinate> filledTiles;
    int prevFilledTiles;
    private Integer prevFilledDistance;

    /**
     * Constructs a Map from the given dimensions and cell array, validating constraints.
     *
     * @param rows  Number of rows
     * @param cols  Number of columns
     * @param cells Pre-populated cell array
     * @throws IllegalArgumentException if map constraints are violated
     */
    public Map(int rows, int cols, Cell[][] cells) {
        this.rows = rows;
        this.cols = cols;
        this.cells = cells;
        this.filledTiles = new HashSet<>();
        this.prevFilledTiles = 0;
        this.prevFilledDistance = null;

        findTerminations();
        validateMap();
    }

    /**
     * Factory method that creates a Map from a string representation.
     *
     * @param rows    Number of rows
     * @param cols    Number of columns
     * @param cellsRep String representation of the cells
     * @return New Map instance
     */
    static Map fromString(int rows, int cols, String cellsRep) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Map(rows, cols, cells);
    }

    /**
     * Scans the cell array to find SOURCE and SINK termination cells.
     */
    private void findTerminations() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                if (cell instanceof TerminationCell) {
                    TerminationCell tc = (TerminationCell) cell;
                    if (tc.type == TerminationType.SOURCE) {
                        if (sourceCell != null) {
                            throw new IllegalArgumentException("Multiple SOURCE cells found");
                        }
                        sourceCell = tc;
                    } else if (tc.type == TerminationType.SINK) {
                        if (sinkCell != null) {
                            throw new IllegalArgumentException("Multiple SINK cells found");
                        }
                        sinkCell = tc;
                    }
                }
            }
        }
    }

    /**
     * Validates map construction constraints.
     */
    private void validateMap() {
        if (sourceCell == null) {
            throw new IllegalArgumentException("Map must contain exactly one SOURCE cell");
        }
        if (sinkCell == null) {
            throw new IllegalArgumentException("Map must contain exactly one SINK cell");
        }

        // SOURCE must be in a non-edge cell
        if (isEdgeCell(sourceCell.coord)) {
            throw new IllegalArgumentException("SOURCE must be in a non-edge cell");
        }

        // SINK must be in an edge cell
        if (!isEdgeCell(sinkCell.coord)) {
            throw new IllegalArgumentException("SINK must be in an edge cell");
        }

        // SOURCE must not point into a wall
        Coordinate sourceTarget = sourceCell.coord.add(sourceCell.pointingTo.getOffset());
        if (!isInBounds(sourceTarget)) {
            throw new IllegalArgumentException("SOURCE points outside the map");
        }
        if (getCell(sourceTarget.row, sourceTarget.col) instanceof Wall) {
            throw new IllegalArgumentException("SOURCE must not point into a wall");
        }

        // SINK must point outside the map
        Coordinate sinkTarget = sinkCell.coord.add(sinkCell.pointingTo.getOffset());
        if (isInBounds(sinkTarget)) {
            throw new IllegalArgumentException("SINK must point outside the map");
        }
    }

    /**
     * Checks if a coordinate is on the edge of the map.
     */
    private boolean isEdgeCell(Coordinate coord) {
        return coord.row == 0 || coord.row == rows - 1 || coord.col == 0 || coord.col == cols - 1;
    }

    /**
     * Returns whether the given coordinate is within map bounds.
     */
    private boolean isInBounds(Coordinate coord) {
        return coord.row >= 0 && coord.row < rows && coord.col >= 0 && coord.col < cols;
    }

    /**
     * Gets the cell at the specified row and column with bounds check.
     *
     * @param row Row index
     * @param col Column index
     * @return Cell at the position, or null if out of bounds
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return cells[row][col];
    }

    /**
     * Gets the cell at the specified coordinate with bounds check.
     *
     * @param coord Coordinate
     * @return Cell at the position, or null if out of bounds
     */
    public Cell getCell(Coordinate coord) {
        return getCell(coord.row, coord.col);
    }

    /**
     * Tries to place a pipe at the given coordinate.
     *
     * @param coord Target coordinate
     * @param pipe  Pipe to place
     * @return true if placement succeeded
     */
    public boolean tryPlacePipe(Coordinate coord, Pipe pipe) {
        return tryPlacePipe(coord.row, coord.col, pipe);
    }

    /**
     * Tries to place a pipe at the given row and column.
     *
     * @param row Row index
     * @param col Column index
     * @param p   Pipe to place
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
     * Removes the pipe from the cell at the given coordinate.
     *
     * @param coord Coordinate to undo
     */
    public void undo(Coordinate coord) {
        Cell cell = getCell(coord);
        if (cell instanceof FillableCell) {
            ((FillableCell) cell).setPipe(null);
        }
    }

    /**
     * Marks the source cell as filled with water.
     */
    public void fillBeginTile() {
        sourceCell.setFilled();
        filledTiles.add(sourceCell.coord);
    }

    /**
     * Expands water flow up to the given distance from source using BFS.
     * Only flows through connected pipes with matching directions.
     *
     * @param distance Max distance from source to fill
     */
    public void fillTiles(int distance) {
        // BFS from source, limited by distance
        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        java.util.Map<Coordinate, Integer> distMap = new HashMap<>();

        Coordinate srcCoord = sourceCell.coord;
        queue.add(srcCoord);
        distMap.put(srcCoord, 0);
        visited.add(srcCoord);

        int newlyFilled = 0;

        while (!queue.isEmpty()) {
            Coordinate curr = queue.poll();
            int currDist = distMap.get(curr);

            if (currDist >= distance) {
                continue;
            }

            Cell currCell = getCell(curr);
            Direction[] exitDirs = getConnectionDirections(currCell);
            if (exitDirs == null) {
                continue;
            }

            for (Direction dir : exitDirs) {
                Coordinate next = curr.add(dir.getOffset());
                if (!isInBounds(next) || visited.contains(next)) {
                    continue;
                }

                Cell nextCell = getCell(next);
                if (nextCell instanceof Wall) {
                    continue;
                }

                // Check if next cell accepts flow from the opposite direction
                Direction[] entryDirs = getConnectionDirections(nextCell);
                if (entryDirs == null) {
                    continue;
                }

                boolean matchFound = false;
                for (Direction entryDir : entryDirs) {
                    if (entryDir == dir.getOpposite()) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    continue;
                }

                // Valid connection: mark as visited and potentially fill
                visited.add(next);
                distMap.put(next, currDist + 1);

                // Fill the cell if not already filled
                if (!filledTiles.contains(next)) {
                    filledTiles.add(next);
                    newlyFilled++;

                    if (nextCell instanceof FillableCell) {
                        ((FillableCell) nextCell).getPipe().ifPresent(p -> p.setFilled(true));
                    } else if (nextCell instanceof TerminationCell) {
                        ((TerminationCell) nextCell).setFilled();
                    }
                }

                queue.add(next);
            }
        }

        prevFilledTiles = newlyFilled;
    }

    /**
     * Checks if a connected path exists from SOURCE to SINK through filled pipes.
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
            Coordinate curr = queue.poll();
            Cell currCell = getCell(curr);

            // Check if we've reached the sink
            if (currCell instanceof TerminationCell
                    && ((TerminationCell) currCell).type == TerminationType.SINK) {
                return ((TerminationCell) currCell).isFilled();
            }

            Direction[] exitDirs = getConnectionDirections(currCell);
            if (exitDirs == null) {
                continue;
            }

            for (Direction dir : exitDirs) {
                Coordinate next = curr.add(dir.getOffset());
                if (!isInBounds(next) || visited.contains(next)) {
                    continue;
                }

                // Only traverse through filled tiles
                if (!filledTiles.contains(next)) {
                    continue;
                }

                Cell nextCell = getCell(next);
                if (nextCell instanceof Wall) {
                    continue;
                }

                // Check matching connections
                Direction[] entryDirs = getConnectionDirections(nextCell);
                if (entryDirs == null) {
                    continue;
                }

                boolean matchFound = false;
                for (Direction entryDir : entryDirs) {
                    if (entryDir == dir.getOpposite()) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    continue;
                }

                visited.add(next);
                queue.add(next);
            }
        }

        return false;
    }

    /**
     * Returns whether the player has lost (no new tiles filled in previous round).
     *
     * @return true if lost
     */
    public boolean hasLost() {
        return prevFilledTiles == 0;
    }

    /**
     * Returns the connection directions for a cell.
     *
     * @param cell The cell
     * @return Array of directions, or null if the cell has no connections (Wall)
     */
    private Direction[] getConnectionDirections(Cell cell) {
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            if (fc.getPipe().isEmpty()) {
                return null;
            }
            return fc.getPipe().get().getConnections();
        }
        if (cell instanceof TerminationCell) {
            return new Direction[]{((TerminationCell) cell).pointingTo};
        }
        return null; // Wall or unknown
    }

    /**
     * Displays the map with row/column labels.
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

    /**
     * Returns the number of rows in this map.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in this map.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the source cell.
     */
    public TerminationCell getSourceCell() {
        return sourceCell;
    }

    /**
     * Returns the sink cell.
     */
    public TerminationCell getSinkCell() {
        return sinkCell;
    }
}
