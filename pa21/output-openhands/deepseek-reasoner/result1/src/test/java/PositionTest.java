import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Position} and {@link PositionOffset}.
 */
public class PositionTest {

    @Test
    public void testPositionOffsetBy() {
        final Position pos = new Position(1, 2);
        final Position result = pos.offsetBy(1, 0);
        assertEquals(new Position(2, 2), result);
    }

    @Test
    public void testPositionOffsetByOffset() {
        final Position pos = new Position(1, 2);
        final PositionOffset offset = new PositionOffset(-1, 1);
        final Position result = pos.offsetBy(offset);
        assertEquals(new Position(0, 3), result);
    }

    @Test
    public void testPositionOffsetByOrNullInBounds() {
        final Position pos = new Position(1, 1);
        final Position result = pos.offsetByOrNull(0, 1, 3, 3);
        assertNotNull(result);
        assertEquals(new Position(1, 2), result);
    }

    @Test
    public void testPositionOffsetByOrNullOutOfBounds() {
        final Position pos = new Position(2, 2);
        assertNull(pos.offsetByOrNull(0, 1, 3, 3));
        assertNull(pos.offsetByOrNull(1, 0, 3, 3));
        assertNull(pos.offsetByOrNull(0, -1, 3, 0));
    }

    @Test
    public void testDirectionGetOffset() {
        assertEquals(new PositionOffset(-1, 0), Direction.UP.getOffset());
        assertEquals(new PositionOffset(1, 0), Direction.DOWN.getOffset());
        assertEquals(new PositionOffset(0, -1), Direction.LEFT.getOffset());
        assertEquals(new PositionOffset(0, 1), Direction.RIGHT.getOffset());
    }

    @Test
    public void testPositionEquality() {
        assertEquals(new Position(1, 2), new Position(1, 2));
        assertNotEquals(new Position(1, 2), new Position(2, 1));
    }

    @Test
    public void testPositionOffsetEquality() {
        assertEquals(new PositionOffset(1, 2), new PositionOffset(1, 2));
        assertNotEquals(new PositionOffset(1, 2), new PositionOffset(2, 1));
    }
}
