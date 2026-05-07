import java.util.List;

/**
 * Main game class that orchestrates pipe placement, water flow,
 * and win/loss checking.
 */
public class Game {

    private final Map map;
    private final CellStack cellStack;
    private final PipeQueue pipeQueue;
    private final DelayBar delayBar;
    private int stepCount;
    private int fillDistance;

    /**
     * Constructs a new Game.
     *
     * @param rows  Number of rows
     * @param cols  Number of columns
     * @param delay Initial delay countdown value
     * @param cells Cell array
     * @param pipes Initial list of pipes for the queue
     */
    public Game(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        this.map = new Map(rows, cols, cells);
        this.cellStack = new CellStack();
        this.pipeQueue = (pipes != null && !pipes.isEmpty())
                ? new PipeQueue(pipes)
                : new PipeQueue();
        this.delayBar = new DelayBar(delay);
        this.stepCount = 0;
        this.fillDistance = 0;
    }

    /**
     * Factory method creating a Game from string representations.
     *
     * @param rows    Number of rows
     * @param cols    Number of columns
     * @param delay   Initial delay
     * @param cellsRep String representation of cells
     * @param pipes   Initial pipe list
     * @return New Game instance
     */
    static Game fromString(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Places the current pipe from the queue onto the map at the given position.
     *
     * @param row Row index
     * @param col Column index
     * @return true if placement succeeded
     */
    public boolean placePipe(int row, int col) {
        Pipe currentPipe = pipeQueue.peek();
        if (currentPipe == null) {
            return false;
        }

        Coordinate coord = new Coordinate(row, col);
        if (!map.tryPlacePipe(coord, currentPipe)) {
            return false;
        }

        // Update state on success
        pipeQueue.consume();
        Cell cell = map.getCell(coord);
        if (cell instanceof FillableCell) {
            cellStack.push((FillableCell) cell);
        }
        stepCount++;
        return true;
    }

    /**
     * Skips the current pipe, consuming it from the queue.
     */
    public void skipPipe() {
        pipeQueue.consume();
        stepCount++;
    }

    /**
     * Undoes the last pipe placement.
     *
     * @return true if an undo was performed, false if there are no steps to undo
     */
    public boolean undoStep() {
        FillableCell lastCell = cellStack.pop();
        if (lastCell == null) {
            return false;
        }

        // Get the pipe that was placed
        Pipe undonePipe = lastCell.getPipe().orElse(null);
        // Clear the cell on the map
        map.undo(lastCell.coord);
        // Restore the pipe to the front of the queue
        if (undonePipe != null) {
            pipeQueue.undo(undonePipe);
        }
        stepCount++;
        return true;
    }

    /**
     * Advances water flow by one round: countdown delay, then fill tiles
     * and check win/loss.
     */
    public void advanceWater() {
        delayBar.countdown();
        if (delayBar.distance() >= 0) {
            fillDistance++;
            map.fillTiles(fillDistance);
        }
    }

    /**
     * Checks if the player has won (path exists from SOURCE to SINK through filled pipes).
     *
     * @return true if win condition met
     */
    public boolean checkWin() {
        return map.checkPath();
    }

    /**
     * Checks if the player has lost (no new tiles filled in previous round).
     * Only meaningful after the delay has ended.
     *
     * @return true if loss condition met
     */
    public boolean checkLoss() {
        return delayBar.distance() >= 0 && map.hasLost();
    }

    /**
     * Returns the current step count.
     *
     * @return Step count
     */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Returns the underlying map.
     *
     * @return Map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Returns the pipe queue.
     *
     * @return PipeQueue
     */
    public PipeQueue getPipeQueue() {
        return pipeQueue;
    }

    /**
     * Returns the cell stack.
     *
     * @return CellStack
     */
    public CellStack getCellStack() {
        return cellStack;
    }

    /**
     * Returns the delay bar.
     *
     * @return DelayBar
     */
    public DelayBar getDelayBar() {
        return delayBar;
    }

    /**
     * Displays the current game state.
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
