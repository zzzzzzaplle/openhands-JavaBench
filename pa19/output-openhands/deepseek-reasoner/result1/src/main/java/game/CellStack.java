package game;

import game.map.FillableCell;

import java.util.Stack;

/**
 * Tracks pipe placements for undo support.
 */
public class CellStack {

    private final Stack<FillableCell> cellStack;
    private int count;

    /**
     * Constructs an empty CellStack.
     */
    public CellStack() {
        this.cellStack = new Stack<>();
        this.count = 0;
    }

    /**
     * Pushes a cell onto the stack.
     *
     * @param cell cell to push
     */
    public void push(FillableCell cell) {
        cellStack.push(cell);
        count++;
    }

    /**
     * Pops the last placed cell from the stack.
     *
     * @return the popped cell, or null if stack is empty
     */
    public FillableCell pop() {
        if (cellStack.isEmpty()) {
            return null;
        }
        return cellStack.pop();
    }

    /**
     * Returns the number of undo operations performed (used for step counting).
     *
     * @return undo count
     */
    public int getUndoCount() {
        return count;
    }

    /**
     * Returns whether the stack is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return cellStack.isEmpty();
    }

    /**
     * Displays the stack info.
     */
    public void display() {
        System.out.println("Undo Count: " + count);
    }
}
