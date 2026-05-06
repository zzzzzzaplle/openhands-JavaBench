package game.map;

import java.util.Stack;

/**
 * Stack to track placed cells for undo functionality.
 */
public class CellStack {

    private final Stack<FillableCell> cellStack;
    private int count;

    public CellStack() {
        this.cellStack = new Stack<>();
        this.count = 0;
    }

    /**
     * Pushes a cell onto the stack.
     *
     * @param cell The cell to push.
     */
    public void push(FillableCell cell) {
        cellStack.push(cell);
        count++;
    }

    /**
     * Pops the last placed cell from the stack.
     *
     * @return The popped cell, or null if empty.
     */
    public FillableCell pop() {
        if (cellStack.isEmpty()) {
            return null;
        }
        count--;
        return cellStack.pop();
    }

    /**
     * Gets the number of undo operations available.
     *
     * @return The undo count.
     */
    public int getUndoCount() {
        return count;
    }

    /**
     * Displays the stack info.
     */
    public void display() {
        System.out.println("Undo Count: " + count);
    }
}