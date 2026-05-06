package game.map;

import game.Direction;
import game.TerminationType;
import game.pipe.Pipe;
import game.util.Deserializer;
import game.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The game map containing all cells and managing pipe placement and water flow.
 */
public class Map {

    private final int rows;
    private final int cols;
    public final Cell[][] cells;
    private final TerminationCell sourceCell;
    private final TerminationCell sinkCell;
    private final Set<Coordinate> filledTiles;
    private int prevFilledTiles;
    private Integer prevFilledDistance;

    /**
     * Creates a map from string representation.
     *
     * @param rows     number of rows
     * @param cols     number of columns
     * @param cellsRep multiline string representation of cells
     * @return constructed Map
     */
    public static Map fromString(int rows, int cols, String cellsRep) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Map(rows, cols, cells);
    }

    /**
     * Constructs a new Map, validating constraints.
     *
     * @param rows  number of rows
     * @param cols  number of columns
     * @param cells 2D array of cells
     * @throws IllegalArgumentException if map constraints are violated
     */
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
                            throw new IllegalArgumentException("Multiple SOURCE cells found");
                        }
                        if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                            throw new IllegalArgumentException("SOURCE must be in a non-edge cell");
                        }
                        Direction dir = tc.pointingTo;
                        int nr = r + dir.getOffset().row;
                        int nc = c + dir.getOffset().col;
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                            throw new IllegalArgumentException("SOURCE points outside the map");
                        }
                        if (cells[nr][nc] instanceof Wall) {
                            throw new IllegalArgumentException("SOURCE must not point into a wall");
                        }
                        foundSource = tc;
                    } else if (tc.type == TerminationType.SINK) {
                        if (foundSink != null) {
                            throw new IllegalArgumentException("Multiple SINK cells found");
                        }
                        boolean onEdge = r == 0 || r == rows - 1 || c == 0 || c == cols - 1;
                        if (!onEdge) {
                            throw new IllegalArgumentException("SINK must be on an edge cell");
                        }
                        Direction dir = tc.pointingTo;
                        int nr = r + dir.getOffset().row;
                        int nc = c + dir.getOffset().col;
                        if (!(nr < 0 || nr >= rows || nc < 0 || nc >= cols)) {
                            throw new IllegalArgumentException("SINK must point outside the map");
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

        this.sourceCell = foundSource;
        this.sinkCell = foundSink;
    }

    /**
     * Returns the cell at the given row and column with bounds checking.
     *
     * @param row row index
     * @param col column index
     * @return cell at the position, or null if out of bounds
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return cells[row][col];
    }

    /**
     * Sets the cell at the given row and column with bounds checking.
     *
     * @param row  row index
     * @param col  column index
     * @param cell cell to set
     */
    public void setCell(int row, int col, Cell cell) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cells[row][col] = cell;
        }
    }

    /**
     * Tries to place a pipe at the given row and column.
     *
     * @param row row index
     * @param col column index
     * @param p   pipe to place
     * @return true if placement succeeded
     */
    public boolean tryPlacePipe(int row, int col, Pipe p) {
        Cell cell = getCell(row, col);
        if (cell == null) {
            return false;
        }
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
     *
     * @param coord coordinate
     * @param pipe  pipe to place
     * @return true if placement succeeded
     */
    public boolean tryPlacePipe(Coordinate coord, Pipe pipe) {
        return tryPlacePipe(coord.row, coord.col, pipe);
    }

    /**
     * Undoes a pipe placement at the given coordinate (clears the cell).
     *
     * @param coord coordinate to clear
     */
    public void undo(Coordinate coord) {
        Cell cell = getCell(coord.row, coord.col);
        if (cell instanceof FillableCell) {
            ((FillableCell) cell).setPipe(null);
        }
    }

    /**
     * Marks the source cell as filled and adds it to filled tiles.
     */
    public void fillBeginTile() {
        sourceCell.setFilled();
        filledTiles.add(sourceCell.coord);
    }

    /**
     * Fills pipes within the specified distance from the source using BFS expansion.
     * Water propagates along pipes only when adjacent pipes have matching connection directions.
     *
     * @param distance distance to fill
     */
    public void fillTiles(int distance) {
        if (distance <= 0) {
            return;
        }

        // If we've already filled to this distance or further, skip
        if (prevFilledDistance != null && distance <= prevFilledDistance) {
            prevFilledTiles = 0;
            return;
        }

        int prevSize = filledTiles.size();

        // BFS from source
        Queue<BfsEntry> queue = new LinkedList<>();
        Set<Coordinate> visited = new HashSet<>();
        visited.add(sourceCell.coord);
        queue.add(new BfsEntry(sourceCell, 0));

        while (!queue.isEmpty()) {
            BfsEntry entry = queue.poll();
            Cell current = entry.cell;
            int currentDist = entry.distance;

            if (currentDist >= distance) {
                continue;
            }

            // Determine which directions to explore
            Direction[] exploreDirs;
            if (current instanceof TerminationCell) {
                TerminationCell tc = (TerminationCell) current;
                if (tc.type == TerminationType.SOURCE) {
                    // Source points in its direction; water flows that way
                    exploreDirs = new Direction[]{tc.pointingTo};
                } else {
                    // SINK - no further exploration from sink
                    continue;
                }
            } else if (current instanceof FillableCell) {
                FillableCell fc = (FillableCell) current;
                if (!fc.getPipe().isPresent() || !fc.isFilled()) {
                    continue;
                }
                exploreDirs = fc.getPipe().get().getConnections();
            } else {
                continue;
            }

            for (Direction dir : exploreDirs) {
                Coordinate nextCoord = current.coord.add(dir.getOffset());
                if (visited.contains(nextCoord)) {
                    continue;
                }

                Cell nextCell = getCell(nextCoord.row, nextCoord.col);
                if (nextCell == null) {
                    continue;
                }

                if (nextCell instanceof FillableCell) {
                    FillableCell nextFc = (FillableCell) nextCell;
                    if (!nextFc.getPipe().isPresent()) {
                        continue;
                    }
                    // Check if connections match
                    Direction[] nextConns = nextFc.getPipe().get().getConnections();
                    boolean matches = false;
                    for (Direction nextDir : nextConns) {
                        if (nextDir == dir.getOpposite()) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches) {
                        continue;
                    }

                    visited.add(nextCoord);
                    nextFc.setFilled();
                    filledTiles.add(nextCoord);
                    queue.add(new BfsEntry(nextFc, currentDist + 1));

                } else if (nextCell instanceof TerminationCell) {
                    // Can reach sink, but don't fill it; mark as visited
                    visited.add(nextCoord);
                    queue.add(new BfsEntry(nextCell, currentDist + 1));
                }
            }
        }

        prevFilledTiles = filledTiles.size() - prevSize;
        prevFilledDistance = distance;
    }

    /**
     * Checks if a connected path exists from SOURCE to SINK through filled pipes.
     *
     * @return true if a path exists (win condition)
     */
    public boolean checkPath() {
        // BFS from source to sink through filled pipes
        Queue<Cell> queue = new LinkedList<>();
        Set<Coordinate> visited = new HashSet<>();
        visited.add(sourceCell.coord);
        queue.add(sourceCell);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            // Check if current cell is adjacent to sink with matching connections
            if (isAdjacentToSink(current)) {
                return true;
            }

            // Determine explore directions
            Direction[] exploreDirs;
            if (current instanceof TerminationCell) {
                TerminationCell tc = (TerminationCell) current;
                if (tc.type == TerminationType.SOURCE) {
                    exploreDirs = new Direction[]{tc.pointingTo};
                } else {
                    continue;
                }
            } else if (current instanceof FillableCell) {
                FillableCell fc = (FillableCell) current;
                if (!fc.getPipe().isPresent() || !fc.isFilled()) {
                    continue;
                }
                exploreDirs = fc.getPipe().get().getConnections();
            } else {
                continue;
            }

            for (Direction dir : exploreDirs) {
                Coordinate nextCoord = current.coord.add(dir.getOffset());
                if (visited.contains(nextCoord)) {
                    continue;
                }

                Cell nextCell = getCell(nextCoord.row, nextCoord.col);
                if (nextCell == null) {
                    continue;
                }

                if (nextCell instanceof FillableCell) {
                    FillableCell nextFc = (FillableCell) nextCell;
                    if (!nextFc.getPipe().isPresent() || !nextFc.isFilled()) {
                        continue;
                    }
                    // Check matching connections
                    Direction[] nextConns = nextFc.getPipe().get().getConnections();
                    boolean matches = false;
                    for (Direction nextDir : nextConns) {
                        if (nextDir == dir.getOpposite()) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches) {
                        continue;
                    }

                    visited.add(nextCoord);
                    queue.add(nextFc);

                } else if (nextCell instanceof TerminationCell) {
                    visited.add(nextCoord);
                    queue.add(nextCell);
                }
            }
        }

        return false;
    }

    /**
     * Checks if the given cell is adjacent to the sink with matching connections.
     */
    private boolean isAdjacentToSink(Cell cell) {
        if (cell instanceof TerminationCell && ((TerminationCell) cell).type == TerminationType.SINK) {
            return true;
        }

        Direction[] checkDirs;
        if (cell instanceof FillableCell) {
            FillableCell fc = (FillableCell) cell;
            if (!fc.getPipe().isPresent() || !fc.isFilled()) {
                return false;
            }
            checkDirs = fc.getPipe().get().getConnections();
        } else if (cell instanceof TerminationCell) {
            checkDirs = new Direction[]{((TerminationCell) cell).pointingTo};
        } else {
            return false;
        }

        for (Direction dir : checkDirs) {
            Coordinate adjCoord = cell.coord.add(dir.getOffset());
            Cell adjCell = getCell(adjCoord.row, adjCoord.col);
            if (adjCell == sinkCell) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if no new pipes were filled in the previous round (loss condition).
     *
     * @return true if the player has lost
     */
    public boolean hasLost() {
        return prevFilledTiles == 0;
    }

    /**
     * Returns the number of new tiles filled in the previous round.
     *
     * @return previous filled tiles count
     */
    public int getPrevFilledTiles() {
        return prevFilledTiles;
    }

    /**
     * Returns the set of filled tile coordinates.
     *
     * @return filled tile coordinates
     */
    public Set<Coordinate> getFilledTiles() {
        return filledTiles;
    }

    /**
     * Returns the number of rows.
     *
     * @return rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns.
     *
     * @return cols
     */
    public int getCols() {
        return cols;
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

    /**
     * BFS entry holding a cell and its distance from source.
     */
    private static class BfsEntry {
        final Cell cell;
        final int distance;

        BfsEntry(Cell cell, int distance) {
            this.cell = cell;
            this.distance = distance;
        }
    }
}
