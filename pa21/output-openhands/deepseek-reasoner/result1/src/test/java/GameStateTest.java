import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GameState}.
 */
public class GameStateTest {

    private GameState loadGameState(final String boardData) {
        try {
            return GameStateSerializer.loadFrom(new BufferedReader(new StringReader(boardData)));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUnlimitedLives() {
        final String board = "1\n3\n-1\nP..\n";
        final GameState state = loadGameState(board);
        assertEquals(Integer.MAX_VALUE, state.getNumLives());
        assertFalse(state.hasLost());
    }

    @Test
    public void testLimitedLives() {
        final String board = "1\n3\n3\nP..\n";
        final GameState state = loadGameState(board);
        assertEquals(3, state.getNumLives());
        assertFalse(state.hasLost());
    }

    @Test
    public void testDecrementLives() {
        final String board = "1\n3\n3\nP..\n";
        final GameState state = loadGameState(board);
        state.decrementNumLives();
        assertEquals(2, state.getNumLives());
    }

    @Test
    public void testHasLost() {
        final String board = "1\n3\n0\nP..\n";
        final GameState state = loadGameState(board);
        assertTrue(state.hasLost());
    }

    @Test
    public void testHasWonWithNoGems() {
        final String board = "1\n3\n-1\nP..\n";
        final GameState state = loadGameState(board);
        // No gems on board, initialNumOfGems = 0, so cannot win
        assertFalse(state.hasWon());
    }

    @Test
    public void testHasWonAfterCollectingAllGems() {
        final String board = "3\n3\n-1\n...\nPG.\n...\n";
        final GameState state = loadGameState(board);
        assertFalse(state.hasWon());
        state.getGameBoardController().makeMove(Direction.RIGHT);
        assertTrue(state.hasWon());
    }

    @Test
    public void testScoreCalculation() {
        // Board 2x3 = 6 cells, 1 gem
        final String board = "2\n3\n-1\nPG.\n...\n";
        final GameState state = loadGameState(board);
        // Initial score: boardSize 6 + (0 * 10) - (0 * 1) - (0 * 2) - (0 * 4) = 6
        assertEquals(6, state.getScore());

        // Move right via GameController to track stats properly
        final GameController controller = new GameController(state);
        controller.processMove(Direction.RIGHT);
        // Player slides to (0,2) rightmost, collected 1 gem
        // Score: 6 + (1*10) - (1*1) - (0*2) - (0*4) = 15
        assertTrue(state.hasWon());
        assertEquals(15, state.getScore());
    }

    @Test
    public void testUnlimitedLivesDecrementDoesNothing() {
        final String board = "1\n3\n-1\nP..\n";
        final GameState state = loadGameState(board);
        state.decrementNumLives();
        assertEquals(Integer.MAX_VALUE, state.getNumLives());
    }

    @Test
    public void testHasUnlimitedLives() {
        final String board = "1\n3\n-1\nP..\n";
        final GameState state = loadGameState(board);
        assertTrue(state.hasUnlimitedLives());
    }

    @Test
    public void testDoesNotHaveUnlimitedLives() {
        final String board = "1\n3\n3\nP..\n";
        final GameState state = loadGameState(board);
        assertFalse(state.hasUnlimitedLives());
    }
}
