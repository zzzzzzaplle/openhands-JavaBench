import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A stack that stores alive move results for undo functionality.
 * Only {@link Alive} results are pushed onto this stack.
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
     * Pushes an alive move result onto the stack.
     *
     * @param move The alive move result to push.
     */
    public void push(final Alive move) {
        stack.push(move);
    }

    /**
     * Pops the most recent alive move result from the stack.
     *
     * @return The most recent alive move result, or {@code null} if the stack is empty.
     */
    public Alive pop() {
        final Alive move = stack.poll();
        if (move != null) {
            popCount++;
        }
        return move;
    }

    /**
     * Returns the number of times {@link #pop()} has been called.
     *
     * @return The pop count.
     */
    public int getPopCount() {
        return popCount;
    }

    /**
     * Returns whether the stack is empty.
     *
     * @return {@code true} if the stack is empty.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return The stack size.
     */
    public int size() {
        return stack.size();
    }
}
