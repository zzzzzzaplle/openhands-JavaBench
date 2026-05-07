import java.util.List;

/**
 * Orchestrates the pipe-connection game.
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
        if (pipes != null && !pipes.isEmpty()) {
            this.pipeQueue = new PipeQueue(3, pipes);
        } else {
            this.pipeQueue = new PipeQueue(3);
        }
        this.delayBar = new DelayBar(delay);
        this.steps = 0;
    }

    /**
     * Convenience constructor from string representation.
     */
    public static Game fromString(int rows, int cols, int delay, String cellsRep, List<Pipe> pipes) {
        Cell[][] cells = Deserializer.parseString(rows, cols, cellsRep);
        return new Game(rows, cols, delay, cells, pipes);
    }

    /**
     * Places the current pipe from the queue at the given position.
     */
    public boolean placePipe(int row, int col) {
        Coordinate coord = new Coordinate(row, col);
        Pipe currentPipe = pipeQueue.peek();
        if (currentPipe == null) {
            return false;
        }

        if (map.tryPlacePipe(coord, currentPipe)) {
            pipeQueue.consume();
            Cell cell = map.getCell(row, col);
            if (cell instanceof FillableCell) {
                cellStack.push((FillableCell) cell);
            }
            delayBar.countdown();
            steps++;
            return true;
        }
        return false;
    }

    /**
     * Skips the current pipe and increments the step count.
     */
    public void skipPipe() {
        pipeQueue.consume();
        delayBar.countdown();
        steps++;
    }

    /**
     * Undoes the last pipe placement.
     */
    public boolean undoStep() {
        FillableCell cell = cellStack.pop();
        if (cell == null) {
            return false;
        }

        Pipe oldPipe = cell.getPipe().orElse(null);
        if (oldPipe != null) {
            pipeQueue.undo(oldPipe);
        }
        map.undo(cell.coord);
        steps++;
        return true;
    }

    /**
     * Advances water flow by one round.
     */
    public void advanceWaterFlow() {
        if (delayBar.distance() <= 0) {
            // Still in countdown or just reached 0
            map.fillBeginTile();
        }
        int distance = delayBar.distance();
        if (distance > 0) {
            map.fillTiles(distance);
        }
    }

    /**
     * Checks if the player has won (path exists from source to sink).
     */
    public boolean checkWin() {
        return map.checkPath();
    }

    /**
     * Checks if the player has lost (no new tiles filled).
     * Loss is only checked after delay has ended (distance > 0).
     */
    public boolean checkLoss() {
        return delayBar.distance() > 0 && map.hasLost();
    }

    public int getSteps() {
        return steps;
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

    public void display() {
        map.display();
        System.out.println();
        pipeQueue.display();
        cellStack.display();
        System.out.println();
        delayBar.display();
    }
}
