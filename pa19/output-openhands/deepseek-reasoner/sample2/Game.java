import java.util.List;

/**
 * Main game class orchestrating the map, pipe queue, cell stack, and delay bar.
 */
public class Game {

    private final Map map;
    private final CellStack cellStack;
    private final PipeQueue pipeQueue;
    private final DelayBar delayBar;
    private int stepCount;

    public Game(int rows, int cols, int delay, Cell[][] cells, List<Pipe> pipes) {
        this.map = new Map(rows, cols, cells);
        this.cellStack = new CellStack();
        this.pipeQueue = pipes != null && !pipes.isEmpty() ? new PipeQueue(pipes) : new PipeQueue();
        this.delayBar = new DelayBar(delay);
        this.stepCount = 0;
    }

    /**
     * Creates a Game from string representation.
     */
    public static Game fromString(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Places a pipe at the given row and column.
     * If successful, updates the pipe queue, delay bar, cell stack, and step count.
     */
    public boolean placePipe(int row, int col) {
        Coordinate coord = new Coordinate(row, col);
        Pipe currentPipe = pipeQueue.peek();

        if (map.tryPlacePipe(coord, currentPipe)) {
            FillableCell cell = (FillableCell) map.getCell(row, col);
            cellStack.push(cell);
            pipeQueue.consume();
            stepCount++;
            return true;
        }
        return false;
    }

    /**
     * Consumes the current pipe from the queue and increments the step count.
     */
    public void skipPipe() {
        pipeQueue.consume();
        stepCount++;
    }

    /**
     * Undoes the last pipe placement.
     *
     * @return false if there are no steps to undo
     */
    public boolean undoStep() {
        if (cellStack.getUndoCount() == 0) {
            return false;
        }

        FillableCell cell = cellStack.pop();
        if (cell != null) {
            cell.getPipe().ifPresent(pipe -> {
                pipeQueue.undo(pipe);
                map.undo(cell.coord);
            });
            stepCount++;
            return true;
        }
        return false;
    }

    /**
     * Advances the water flow by one round.
     * Calls countdown on the delay bar and fills tiles based on distance.
     */
    public void advanceWaterFlow() {
        delayBar.countdown();
        int distance = delayBar.distance();
        if (distance > 0) {
            map.fillTiles(distance);
        }
    }

    /**
     * Checks if the player has won (water path from source to sink exists).
     */
    public boolean checkWin() {
        return map.checkPath();
    }

    /**
     * Checks if the player has lost (no new tiles filled in previous round).
     */
    public boolean checkLoss() {
        return delayBar.distance() > 0 && map.hasLost();
    }

    public int getStepCount() {
        return stepCount;
    }

    public Map getMap() {
        return map;
    }

    public PipeQueue getPipeQueue() {
        return pipeQueue;
    }

    public CellStack getCellStack() {
        return cellStack;
    }

    public DelayBar getDelayBar() {
        return delayBar;
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
