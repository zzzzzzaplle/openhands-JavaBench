import java.util.Stack;

/**
 * Stack that tracks placed cells for undo support.
 */
public class CellStack {

    private final Stack<FillableCell> cellStack;
    private int count;

    public CellStack() {
        this.cellStack = new Stack<>();
        this.count = 0;
    }

    /**
     * Pushes a placed cell onto the stack.
     *
     * @param cell The fillable cell that was placed
     */
    public void push(FillableCell cell) {
        cellStack.push(cell);
        count++;
    }

    /**
     * Pops the most recently placed cell from the stack.
     *
     * @return The cell, or null if stack is empty
     */
    public FillableCell pop() {
        if (cellStack.isEmpty()) {
            return null;
        }
        count--;
        return cellStack.pop();
    }

    /**
     * Returns the number of undo steps available.
     *
     * @return Undo count
     */
    public int getUndoCount() {
        return count;
    }

    /**
     * Displays the current undo count.
     */
    public void display() {
        System.out.println("Undo Count: " + count);
    }
}
