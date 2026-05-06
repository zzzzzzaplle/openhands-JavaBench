import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GameController}.
 */
public class GameControllerTest {

    private GameState loadGameState(final String boardData) {
        try {
            return GameStateSerializer.loadFrom(new BufferedReader(new StringReader(boardData)));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessMoveAliveUpdatesStats() {
        final String board = "3\n3\n3\n...\nP..\n...\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        // Move right - should be alive (slides to right boundary cell (1,2))
        final MoveResult result = controller.processMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);

        // Moves should be incremented
        assertEquals(1, state.getNumMoves());
        // Deaths should not be incremented
        assertEquals(0, state.getNumDeaths());
        // Lives should not change
        assertEquals(3, state.getNumLives());
    }

    @Test
    public void testProcessMoveDeadUpdatesStats() {
        final String board = "3\n3\n3\n...\nPM.\n...\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        final MoveResult result = controller.processMove(Direction.RIGHT);
        assertInstanceOf(Dead.class, result);

        // Moves and deaths should be incremented
        assertEquals(1, state.getNumMoves());
        assertEquals(1, state.getNumDeaths());
        // Lives should be decremented
        assertEquals(2, state.getNumLives());
    }

    @Test
    public void testProcessMoveInvalidDoesNotUpdateStats() {
        final String board = "3\n3\n3\nWWW\nWPW\nWWW\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        final MoveResult result = controller.processMove(Direction.RIGHT);
        assertInstanceOf(Invalid.class, result);

        // Nothing should change
        assertEquals(0, state.getNumMoves());
        assertEquals(0, state.getNumDeaths());
        assertEquals(3, state.getNumLives());
    }

    @Test
    public void testProcessUndo() {
        final String board = "3\n3\n3\n...\nP..\n...\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        // Move right
        controller.processMove(Direction.RIGHT);
        assertEquals(new Position(1, 2), state.getGameBoard().getPlayerPosition());

        // Undo
        final boolean undone = controller.processUndo();
        assertTrue(undone);
        assertEquals(new Position(1, 0), state.getGameBoard().getPlayerPosition());
    }

    @Test
    public void testProcessUndoWithEmptyStack() {
        final String board = "3\n3\n3\n...\nP..\n...\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        assertFalse(controller.processUndo());
    }

    @Test
    public void testDeadMoveNotPushedToStack() {
        final String board = "3\n3\n3\n...\nPM.\n...\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        controller.processMove(Direction.RIGHT);
        // Stack should be empty since Dead moves are not pushed
        assertTrue(state.getMoveStack().isEmpty());
    }

    @Test
    public void testInvalidMoveNotPushedToStack() {
        final String board = "3\n3\n3\nWWW\nWPW\nWWW\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        controller.processMove(Direction.RIGHT);
        assertTrue(state.getMoveStack().isEmpty());
    }

    @Test
    public void testAliveMovePushedToStack() {
        final String board = "3\n3\n3\n...\nP..\n...\n";
        final GameState state = loadGameState(board);
        final GameController controller = new GameController(state);

        controller.processMove(Direction.RIGHT);
        assertEquals(1, state.getMoveStack().size());
    }
}
