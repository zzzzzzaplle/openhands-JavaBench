import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * A stack of undoable (Alive) moves.
 * <p>
 * Only {@link Alive} moves are pushed to this stack. {@link Dead} and {@link Invalid} moves are
 * never pushed.
 */
public class MoveStack {

    private final Deque<Alive> stack = new ArrayDeque<>();
    private int popCount;

    /**
     * Creates an empty move stack.
     */
    public MoveStack() {
    }

    /**
     * Pushes an alive move onto the stack.
     *
     * @param move The move to push.
     */
    public void push(final Alive move) {
        stack.push(Objects.requireNonNull(move));
    }

    /**
     * Pops the most recent alive move from the stack.
     *
     * @return The most recent move.
     * @throws java.util.NoSuchElementException if the stack is empty.
     */
    public Alive pop() {
        popCount++;
        return stack.pop();
    }

    /**
     * Returns whether the stack is empty.
     *
     * @return {@code true} if no moves are on the stack.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns the total number of pop operations performed.
     *
     * @return The pop count.
     */
    public int getPopCount() {
        return popCount;
    }
}
