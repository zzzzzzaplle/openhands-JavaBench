package game.map;

import java.util.List;

/**
 * Represents the game state and operations.
 */
public class Game {

    private final Map map;
    private final CellStack cellStack;
    private final PipeQueue pipeQueue;
    private final DelayBar delayBar;
    private int steps;

    public Game(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        this.map = new Map(rows, cols, cells);
        this.cellStack = new CellStack();
        this.pipeQueue = new PipeQueue(pipes);
        this.delayBar = new DelayBar(delay);
        this.steps = 0;
    }

    /**
     * Creates a game from string representation.
     *
     * @param rows     Number of rows.
     * @param cols    Number of columns.
     * @param delay   Delay value.
     * @param cellsRep String representation of cells.
     * @param pipes   List of default pipes.
     * @return The created game.
     */
    public static Game fromString(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Places a pipe at the given row and column.
     *
     * @param row The row (1-based).
     * @param col The column (1-based).
     * @return True if placement was successful.
     */
    public boolean placePipe(int row, int col) {
        Coordinate coord = new Coordinate(row, col);
        return placePipe(coord);
    }

    /**
     * Places a pipe at the given coordinate.
     *
     * @param coord The coordinate.
     * @return True if placement was successful.
     */
    public boolean placePipe(Coordinate coord) {
        Pipe pipe = pipeQueue.peek();
        if (pipe == null) {
            return false;
        }

        if (map.tryPlacePipe(coord, pipe)) {
            // Update pipe queue
            pipeQueue.consume();

            // Get the cell for the stack
            FillableCell cell = (FillableCell) map.getCell(coord.row, coord.col);
            cellStack.push(cell);

            // Advance delay
            advanceWaterFlow();

            steps++;
            return true;
        }

        return false;
    }

    /**
     * Skips the current pipe from the queue.
     *
     * @return True if skip was successful.
     */
    public boolean skipPipe() {
        Pipe pipe = pipeQueue.peek();
        if (pipe == null) {
            return false;
        }

        pipeQueue.consume();
        advanceWaterFlow();
        steps++;
        return true;
    }

    /**
     * Undoes the last step.
     *
     * @return True if undo was successful.
     */
    public boolean undoStep() {
        FillableCell cell = cellStack.pop();
        if (cell == null) {
            return false;
        }

        // Restore pipe to queue
        Pipe pipe = cell.getPipe().orElse(null);
        if (pipe != null) {
            pipeQueue.undo(pipe);
        }

        // Clear the cell
        map.undo(cell.getCoord());

        advanceWaterFlow();
        steps++;
        return true;
    }

    /**
     * Advances the water flow by one round.
     */
    public void advanceWaterFlow() {
        delayBar.countdown();
        int distance = delayBar.distance();

        if (distance < 0) {
            // Still in countdown phase
            return;
        }

        // Water is flowing
        if (distance == 0) {
            // Just finished countdown, start filling from source
            map.fillBeginTile();
        } else {
            // Continue filling
            map.fillTiles(distance);
        }
    }

    /**
     * Checks if the game has been won.
     *
     * @return True if won.
     */
    public boolean checkWin() {
        return map.checkPath();
    }

    /**
     * Checks if the game has been lost.
     *
     * @return True if lost.
     */
    public boolean hasLost() {
        return delayBar.distance() > 0 && map.hasLost();
    }

    /**
     * Gets the number of steps taken.
     *
     * @return The step count.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Gets the map.
     *
     * @return The map.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Gets the pipe queue.
     *
     * @return The pipe queue.
     */
    public PipeQueue getPipeQueue() {
        return pipeQueue;
    }

    /**
     * Gets the cell stack.
     *
     * @return The cell stack.
     */
    public CellStack getCellStack() {
        return cellStack;
    }

    /**
     * Gets the delay bar.
     *
     * @return The delay bar.
     */
    public DelayBar getDelayBar() {
        return delayBar;
    }

    /**
     * Displays the game state.
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