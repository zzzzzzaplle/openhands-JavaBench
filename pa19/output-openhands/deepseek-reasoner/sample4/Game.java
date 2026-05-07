import java.util.List;

/**
 * The main pipe-connection game controller.
 * Manages the map, pipe queue, undo stack, delay bar, and step count.
 */
public class Game {

    private final Map map;
    private final CellStack cellStack;
    private final PipeQueue pipeQueue;
    private final DelayBar delayBar;
    private int steps;

    /**
     * Constructs a game with the given parameters.
     *
     * @param rows  map rows
     * @param cols  map columns
     * @param delay initial delay countdown
     * @param cells pre-built cell grid
     * @param pipes optional initial pipe list (may be null)
     */
    Game(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        this.map = new Map(rows, cols, cells);
        this.cellStack = new CellStack();
        this.pipeQueue = (pipes != null && !pipes.isEmpty()) ? new PipeQueue(pipes) : new PipeQueue();
        this.delayBar = new DelayBar(delay);
        this.steps = 0;
    }

    /**
     * Creates a game from a string representation.
     *
     * @param rows    number of rows
     * @param cols    number of columns
     * @param delay   initial countdown value
     * @param cellsRep multiline string of cell characters
     * @param pipes   optional initial pipes
     * @return a new Game instance
     */
    static Game fromString(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Places the current pipe from the queue at the given position.
     *
     * @param row target row
     * @param col target column
     * @return true if placement succeeded
     */
    public boolean placePipe(int row, int col) {
        Coordinate coord = new Coordinate(row, col);
        Pipe currentPipe = pipeQueue.peek();
        if (currentPipe == null) {
            return false;
        }
        if (map.tryPlacePipe(coord, currentPipe)) {
            Cell cell = map.getCell(row, col);
            if (cell instanceof FillableCell) {
                cellStack.push((FillableCell) cell);
            }
            pipeQueue.consume();
            steps++;
            return true;
        }
        return false;
    }

    /**
     * Skips (consumes) the current pipe from the queue without placing it.
     */
    public void skipPipe() {
        pipeQueue.consume();
        steps++;
    }

    /**
     * Undoes the most recent pipe placement.
     *
     * @return true if an undo was performed, false if there is nothing to undo
     */
    public boolean undoStep() {
        FillableCell cell = cellStack.pop();
        if (cell == null) {
            return false;
        }
        Pipe restoredPipe = cell.getPipe().orElse(null);
        if (restoredPipe != null) {
            map.undo(cell.coord);
            pipeQueue.undo(restoredPipe);
        }
        steps++;
        return true;
    }

    /**
     * Advances the water flow by one round:
     * counts down the delay bar, then fills tiles if delay has elapsed.
     */
    public void advanceWaterFlow() {
        delayBar.countdown();
        int distance = delayBar.distance();
        if (distance > 0) {
            map.fillTiles(distance);
        }
    }

    /**
     * Checks whether the player has won (a complete path from SOURCE to SINK exists).
     *
     * @return true if the player has won
     */
    public boolean checkWin() {
        return map.checkPath();
    }

    /**
     * Checks whether the player has lost (no new pipes were filled).
     *
     * @return true if the player has lost
     */
    public boolean checkLoss() {
        return distance() > 0 && map.hasLost();
    }

    /**
     * Returns the current water flow distance.
     *
     * @return distance value (negative during countdown)
     */
    public int distance() {
        return delayBar.distance();
    }

    /**
     * Returns the number of steps taken.
     *
     * @return step count
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Returns the underlying map.
     *
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Initializes water flow by marking the source tile.
     */
    public void beginFilling() {
        map.fillBeginTile();
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
