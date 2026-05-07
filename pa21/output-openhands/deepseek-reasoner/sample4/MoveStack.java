import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A stack of moves supporting undo operations.
 * Only {@link Alive} moves are stored (Dead and Invalid are never pushed).
 */
public class MoveStack {

    private final Deque<Alive> stack;
    private int popCount;

    /**
     * Creates an empty move stack.
     */
    public MoveStack() {
        this.stack = new ArrayDeque<>();
        this.popCount = 0;
    }

    /**
     * Pushes an {@link Alive} move onto the stack.
     *
     * @param move The move to push.
     */
    public void push(final Alive move) {
        stack.push(move);
    }

    /**
     * Pops the most recent move from the stack.
     *
     * @return The most recent move, or {@code null} if the stack is empty.
     */
    public Alive pop() {
        if (stack.isEmpty()) {
            return null;
        }
        popCount++;
        return stack.pop();
    }

    /**
     * Peeks at the most recent move without removing it.
     *
     * @return The most recent move, or {@code null} if the stack is empty.
     */
    public Alive peek() {
        return stack.peek();
    }

    /**
     * Returns the number of moves currently in the stack.
     *
     * @return The stack size.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Returns whether the stack is empty.
     *
     * @return {@code true} if empty.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns the number of times pop has been called (undos performed).
     *
     * @return The pop count.
     */
    public int getPopCount() {
        return popCount;
    }

    /**
     * Clears the stack.
     */
    public void clear() {
        stack.clear();
    }
}
