import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A stack of {@link MoveResult.Valid.Alive} moves for undo functionality.
 */
public class MoveStack {

    private final Deque<MoveResult.Valid.Alive> stack = new ArrayDeque<>();
    private int popCount;

    /**
     * Creates an empty move stack.
     */
    public MoveStack() {
        this.popCount = 0;
    }

    /**
     * Pushes an alive move onto the stack.
     *
     * @param move The move to push.
     */
    public void push(final MoveResult.Valid.Alive move) {
        stack.push(move);
    }

    /**
     * Pops the most recent move from the stack.
     *
     * @return The most recent move, or {@code null} if the stack is empty.
     */
    public MoveResult.Valid.Alive pop() {
        final MoveResult.Valid.Alive move = stack.pollFirst();
        if (move != null) {
            popCount++;
        }
        return move;
    }

    /**
     * @return {@code true} if the stack is empty.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * @return The number of times a move has been popped (undone) from this stack.
     */
    public int getPopCount() {
        return popCount;
    }
}
