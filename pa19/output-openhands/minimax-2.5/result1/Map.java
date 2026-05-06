package game.map;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Represents the game map with cells and water flow mechanics.
 */
public class Map {

    private final int rows;
    private final int cols;
    private final Cell[][] cells;
    private TerminationCell sourceCell;
    private TerminationCell sinkCell;
    private final Set<Coordinate> filledTiles;
    private int prevFilledTiles;
    private Integer prevFilledDistance;

    public Map(int rows, int cols, Cell[][] cells) {
        this.rows = rows;
        this.cols = cols;
        this.cells = cells;
        this.filledTiles = new HashSet<>();
        this.prevFilledTiles = 0;
        this.prevFilledDistance = null;

        // Find source and sink cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j] instanceof TerminationCell) {
                    TerminationCell term = (TerminationCell) cells[i][j];
                    if (term.getType() == TerminationType.SOURCE) {
                        this.sourceCell = term;
                    } else {
                        this.sinkCell = term;
                    }
                }
            }
        }
    }

    /**
     * Creates a map from a string representation.
     *
     * @param rows      Number of rows.
     * @param cols     Number of columns.
     * @param cellsRep String representation of cells.
     * @return The created map.
     */
    public static Map fromString(int rows, int cols, String cellsRep) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Map(rows, cols, cells);
    }

    /**
     * Tries to place a pipe at the given coordinates.
     *
     * @param row The row (1-based).
     * @param col The column (1-based).
     * @param p   The pipe to place.
     * @return True if placement was successful.
     */
    public boolean tryPlacePipe(int row, int col, Pipe p) {
        // Check bounds (convert from 1-based to 0-based)
        if (row < 1 || row >= rows || col < 1 || col >= cols) {
            return false;
        }

        Cell cell = cells[row][col];
        if (!(cell instanceof FillableCell)) {
            return false;
        }

        FillableCell fillableCell = (FillableCell) cell;
        if (fillableCell.getPipe().isPresent()) {
            return false;
        }

        fillableCell.setPipe(p);
        return true;
    }

    /**
     * Tries to place a pipe at the given coordinate.
     *
     * @param coord The coordinate.
     * @param pipe  The pipe.
     * @return True if successful.
     */
    public boolean tryPlacePipe(Coordinate coord, Pipe pipe) {
        return tryPlacePipe(coord.row, coord.col, pipe);
    }

    /**
     * Gets a cell at the given coordinates.
     *
     * @param row The row (0-based).
     * @param col The column (0-based).
     * @return The cell, or null if out of bounds.
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return cells[row][col];
    }

    /**
     * Sets a cell at the given coordinates.
     *
     * @param row  The row (0-based).
     * @param col  The column (0-based).
     * @param cell The cell to set.
     */
    public void setCell(int row, int col, Cell cell) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cells[row][col] = cell;
        }
    }

    /**
     * Undoes a cell placement at the given coordinate.
     *
     * @param coord The coordinate.
     */
    public void undo(Coordinate coord) {
        if (coord.row >= 0 && coord.row < rows && coord.col >= 0 && coord.col < cols) {
            Cell cell = cells[coord.row][coord.col];
            if (cell instanceof FillableCell) {
                ((FillableCell) cell).setPipe(null);
            }
        }
    }

    /**
     * Marks the source cell as filled.
     */
    public void fillBeginTile() {
        if (sourceCell != null) {
            sourceCell.setFilled();
            filledTiles.add(sourceCell.getCoord());
        }
    }

    /**
     * Fills pipes within the specified distance from source using BFS-style expansion.
     *
     * @param distance The fill distance.
     */
    public void fillTiles(int distance) {
        if (distance <= 0) {
            return;
        }

        prevFilledTiles = 0;
        Set<Coordinate> currentLevel = new HashSet<>(filledTiles);

        for (int d = 1; d <= distance; d++) {
            Set<Coordinate> nextLevel = new HashSet<>();

            for (Coordinate coord : currentLevel) {
                // Check all four directions
                for (Direction dir : Direction.values()) {
                    Coordinate offset = dir.getOffset();
                    Coordinate newCoord = coord.add(offset);

                    Cell cell = getCell(newCoord.row, newCoord.col);
                    if (cell == null) {
                        continue;
                    }

                    // Check if this is a fillable cell with a pipe
                    if (cell instanceof FillableCell) {
                        FillableCell fillableCell = (FillableCell) cell;
                        if (fillableCell.getPipe().isPresent()) {
                            Pipe pipe = fillableCell.getPipe().get();
                            if (!filledTiles.contains(newCoord)) {
                                // Check if pipe connects to current position
                                Direction opposite = dir.getOpposite();
                                Direction[] connections = pipe.getConnections();
                                for (Direction connDir : connections) {
                                    if (connDir == opposite) {
                                        // Valid connection
                                        pipe.setFilled(true);
                                        filledTiles.add(newCoord);
                                        nextLevel.add(newCoord);
                                        prevFilledTiles++;
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (cell instanceof TerminationCell) {
                        TerminationCell term = (TerminationCell) cell;
                        if (!filledTiles.contains(newCoord)) {
                            Direction opposite = dir.getOpposite();
                            if (term.getPointingTo() == opposite) {
                                term.setFilled();
                                filledTiles.add(newCoord);
                                nextLevel.add(newCoord);
                                prevFilledTiles++;
                            }
                        }
                    }
                }
            }

            currentLevel = nextLevel;
            if (currentLevel.isEmpty()) {
                break;
            }
        }

        prevFilledDistance = distance;
    }

    /**
     * Checks if there's a connected path from SOURCE to SINK.
     *
     * @return True if path exists.
     */
    public boolean checkPath() {
        if (sourceCell == null || sinkCell == null) {
            return false;
        }

        // BFS from source to sink
        Queue<Coordinate> queue = new LinkedList<>();
        Set<Coordinate> visited = new HashSet<>();

        queue.add(sourceCell.getCoord());
        visited.add(sourceCell.getCoord());

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            if (current.equals(sinkCell.getCoord())) {
                return true;
            }

            Cell cell = getCell(current.row, current.col);
            Direction[] directions;

            if (cell instanceof TerminationCell) {
                directions = new Direction[]{((TerminationCell) cell).getPointingTo()};
            } else if (cell instanceof FillableCell) {
                FillableCell fillable = (FillableCell) cell;
                if (fillable.getPipe().isPresent()) {
                    directions = fillable.getPipe().get().getConnections();
                } else {
                    continue;
                }
            } else {
                continue;
            }

            for (Direction dir : directions) {
                Coordinate offset = dir.getOffset();
                Coordinate next = current.add(offset);

                if (!visited.contains(next)) {
                    Cell nextCell = getCell(next.row, next.col);
                    if (nextCell != null) {
                        visited.add(next);
                        queue.add(next);
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if the game has been lost.
     * Returns true when prevFilledTiles == 0 and distance > 0.
     *
     * @return True if lost.
     */
    public boolean hasLost() {
        if (prevFilledDistance == null || prevFilledDistance <= 0) {
            return false;
        }
        return prevFilledTiles == 0;
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

            for (int j = 0; j < cols; ++j) {
                System.out.print(cells[i][j].toSingleChar());
            }

            if (i != 0 && i != rows - 1) {
                System.out.print(i);
            }

            System.out.println();
        }

        printColumns.run();
    }

    /**
     * Gets the number of rows.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns.
     *
     * @return The number of columns.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Gets the source cell.
     *
     * @return The source cell.
     */
    public TerminationCell getSourceCell() {
        return sourceCell;
    }

    /**
     * Gets the sink cell.
     *
     * @return The sink cell.
     */
    public TerminationCell getSinkCell() {
        return sinkCell;
    }
}