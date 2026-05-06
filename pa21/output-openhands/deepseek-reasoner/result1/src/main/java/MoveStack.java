import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Stack for storing valid (Alive) moves to support the Undo feature.
 * Only Alive moves are pushed; Dead and Invalid are never pushed.
 */
public class MoveStack {

    private final Deque<Alive> stack = new ArrayDeque<>();
    private int popCount = 0;

    /**
     * Pushes an Alive move onto the stack.
     *
     * @param move The alive move to push.
     */
    public void push(final Alive move) {
        stack.push(move);
    }

    /**
     * Pops the most recent Alive move from the stack.
     *
     * @return The most recent alive move, or null if the stack is empty.
     */
    public Alive pop() {
        final Alive move = stack.poll();
        if (move != null) {
            popCount++;
        }
        return move;
    }

    /**
     * Returns the number of times pop has been called.
     *
     * @return The pop count.
     */
    public int getPopCount() {
        return popCount;
    }

    /**
     * Returns the number of elements currently on the stack.
     *
     * @return Stack size.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Returns whether the stack is empty.
     *
     * @return True if the stack is empty.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
