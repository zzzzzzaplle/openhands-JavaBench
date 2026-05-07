import java.util.Stack;

/**
 * Tracks pipe placements for undo support.
 */
public class CellStack {

    private final Stack<FillableCell> cellStack;
    private int count;

    public CellStack() {
        this.cellStack = new Stack<>();
        this.count = 0;
    }

    /**
     * Pushes a cell onto the undo stack.
     *
     * @param cell the fillable cell to push
     */
    public void push(FillableCell cell) {
        cellStack.push(cell);
        count++;
    }

    /**
     * Pops the most recently placed cell from the stack.
     *
     * @return the cell, or null if stack is empty
     */
    public FillableCell pop() {
        if (cellStack.isEmpty()) {
            return null;
        }
        count--;
        return cellStack.pop();
    }

    /**
     * Returns the number of undoable placements.
     *
     * @return undo count
     */
    public int getUndoCount() {
        return count;
    }

    /**
     * Displays the undo count.
     */
    public void display() {
        System.out.println("Undo Count: " + count);
    }
}
