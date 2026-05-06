package game;

/**
 * Stack for tracking moves - only Valid.Alive moves are pushed and undoable.
 */
public class MoveStack {
    private int popCount;
    private java.util.Stack<MoveResult> stack;

    public MoveStack() {
        this.stack = new java.util.Stack<>();
        this.popCount = 0;
    }

    /**
     * Push a move result to the stack.
     */
    public void push(MoveResult result) {
        if (result instanceof MoveResult.Valid.Alive) {
            stack.push(result);
        }
    }

    /**
     * Pop and return the most recent move.
     */
    public MoveResult pop() {
        if (stack.isEmpty()) {
            return null;
        }
        popCount++;
        return stack.pop();
    }

    /**
     * Clear the stack.
     */
    public void clear() {
        stack.clear();
        popCount = 0;
    }

    /**
     * Check if the stack is empty.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Get the number of undos available.
     */
    public int availableUndos() {
        return stack.size();
    }

    public int getPopCount() {
        return popCount;
    }
}