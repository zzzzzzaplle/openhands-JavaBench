import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link MoveStack}.
 */
public class MoveStackTest {

    @Test
    public void testPushAndPop() {
        final MoveStack stack = new MoveStack();
        final Alive move = new Alive(new Position(0, 0), new Position(0, 1),
                Collections.emptyList(), Collections.emptyList());

        stack.push(move);
        assertEquals(1, stack.size());
        assertFalse(stack.isEmpty());

        final Alive popped = stack.pop();
        assertSame(move, popped);
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testPopFromEmptyStack() {
        final MoveStack stack = new MoveStack();
        assertNull(stack.pop());
    }

    @Test
    public void testPopCount() {
        final MoveStack stack = new MoveStack();
        final Alive move1 = new Alive(new Position(0, 0), new Position(0, 1),
                Collections.emptyList(), Collections.emptyList());
        final Alive move2 = new Alive(new Position(1, 0), new Position(1, 1),
                Collections.emptyList(), Collections.emptyList());

        stack.push(move1);
        stack.push(move2);
        assertEquals(0, stack.getPopCount());

        stack.pop();
        assertEquals(1, stack.getPopCount());

        stack.pop();
        assertEquals(2, stack.getPopCount());
    }

    @Test
    public void testPopFromEmptyDoesNotIncrementPopCount() {
        final MoveStack stack = new MoveStack();
        assertNull(stack.pop());
        assertEquals(0, stack.getPopCount());
    }
}
