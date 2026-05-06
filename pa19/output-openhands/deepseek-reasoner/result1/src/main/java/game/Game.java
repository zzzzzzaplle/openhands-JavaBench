package game;

import game.map.Cell;
import game.map.Coordinate;
import game.map.FillableCell;
import game.map.Map;
import game.pipe.Pipe;

import java.util.List;

/**
 * Main game class managing map, pipe queue, cell stack, delay bar, and step count.
 */
public class Game {

    private final Map map;
    private final CellStack cellStack;
    private final PipeQueue pipeQueue;
    private final DelayBar delayBar;
    private int steps;
    private boolean waterFlowing;

    /**
     * Convenience constructor from parsed data.
     *
     * @param rows    number of rows
     * @param cols    number of columns
     * @param delay   initial delay value
     * @param cells   2D cell array
     * @param pipes   initial pipe list
     */
    public Game(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        this.map = new Map(rows, cols, cells);
        this.cellStack = new CellStack();
        this.pipeQueue = new PipeQueue(pipes);
        this.delayBar = new DelayBar(delay);
        this.steps = 0;
        this.waterFlowing = false;
    }

    /**
     * Convenience factory method.
     *
     * @param rows    number of rows
     * @param cols    number of columns
     * @param delay   initial delay value
     * @param cellsRep string representation of cells
     * @param pipes   initial pipe list
     * @return new Game instance
     */
    public static Game fromString(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = game.util.Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Places a pipe at the given row and column.
     *
     * @param row  row index (1-based)
     * @param col  column index (1-based)
     * @param pipe pipe to place
     * @return true if placement succeeded
     */
    public boolean placePipe(int row, int col, Pipe pipe) {
        Coordinate coord = new Coordinate(row, col);
        if (map.tryPlacePipe(coord, pipe)) {
            pipeQueue.consume();
            cellStack.push(new FillableCell(coord, pipe));
            steps++;
            return true;
        }
        return false;
    }

    /**
     * Skips the current pipe in the queue.
     */
    public void skipPipe() {
        pipeQueue.consume();
        steps++;
    }

    /**
     * Undoes the last pipe placement.
     *
     * @return true if an undo was performed, false if there is nothing to undo
     */
    public boolean undoStep() {
        if (cellStack.isEmpty()) {
            return false;
        }
        FillableCell lastCell = cellStack.pop();
        if (lastCell == null) {
            return false;
        }
        Pipe pipe = lastCell.getPipe().orElse(null);
        if (pipe == null) {
            return false;
        }
        // Clear the cell on the map
        map.undo(lastCell.coord);
        // Restore the pipe to the queue head
        pipeQueue.undo(pipe);
        steps++;
        return true;
    }

    /**
     * Advances the water flow by one round.
     */
    public void advanceWaterFlow() {
        if (!waterFlowing) {
            map.fillBeginTile();
            waterFlowing = true;
        }
        delayBar.countdown();
        int distance = delayBar.distance();
        if (distance > 0) {
            map.fillTiles(distance);
        }
    }

    /**
     * Checks if the player has won (connected path from SOURCE to SINK).
     *
     * @return true if win condition met
     */
    public boolean checkWin() {
        if (delayBar.distance() <= 0) {
            return false;
        }
        return map.checkPath();
    }

    /**
     * Checks if the player has lost (no new tiles filled in the previous round).
     * Loss is only checked after delay has ended (distance > 0).
     *
     * @return true if loss condition met
     */
    public boolean checkLoss() {
        if (delayBar.distance() <= 0) {
            return false;
        }
        return map.hasLost();
    }

    /**
     * Returns current step count.
     *
     * @return step count
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Displays the full game state.
     */
    public void display() {
        map.display();
        System.out.println();
        pipeQueue.display();
        cellStack.display();
        System.out.println();
        delayBar.display();
    }
}
