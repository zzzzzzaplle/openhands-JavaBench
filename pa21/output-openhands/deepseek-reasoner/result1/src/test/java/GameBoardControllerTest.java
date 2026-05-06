import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GameBoardController}.
 */
public class GameBoardControllerTest {

    private GameState loadGameState(final String boardData) {
        try {
            return GameStateSerializer.loadFrom(new BufferedReader(new StringReader(boardData)));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMoveToWallReturnsInvalid() {
        // Player at (1,1), wall at (1,2) - moving RIGHT is blocked
        final String board = "3\n3\n-1\nWWW\nW.P\nWWW\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Invalid.class, result);
    }

    @Test
    public void testMoveToBoundaryReturnsInvalid() {
        // Player at (0,1), boundary above - moving UP is blocked
        final String board = "3\n3\n-1\n.P.\n...\n...\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.UP);
        assertInstanceOf(Invalid.class, result);
    }

    @Test
    public void testSlideUntilWall() {
        // Player at (1,0), wall at (1,3)
        // Board: .W.P...
        final String board = "3\n4\n-1\n....\nP..W\n....\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;
        assertEquals(new Position(1, 2), alive.getNewPosition());
        assertEquals(new Position(1, 0), alive.getOrigPosition());
    }

    @Test
    public void testSlideUntilStopCell() {
        // Player at (1,0), StopCell at (1,2)
        final String board = "3\n3\n-1\n...\nP.S\n...\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;
        assertEquals(new Position(1, 2), alive.getNewPosition());
    }

    @Test
    public void testCollectGem() {
        // Player at (1,0), Gem at (1,1), wall at (1,3)
        final String board = "3\n4\n-1\n....\nPG.W\n....\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;

        // Should have collected the gem and stopped before wall at (1,2)
        assertEquals(1, alive.getCollectedGems().size());
        assertEquals(new Position(1, 1), alive.getCollectedGems().get(0));
        assertEquals(new Position(1, 2), alive.getNewPosition());

        // Gem should be removed from board
        final Cell cell = state.getGameBoard().getCell(1, 1);
        assertInstanceOf(EntityCell.class, cell);
        assertNull(((EntityCell) cell).getEntity());
    }

    @Test
    public void testCollectExtraLife() {
        final String board = "3\n4\n-1\n....\nPL.W\n....\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;
        assertEquals(1, alive.getCollectedExtraLives().size());
    }

    @Test
    public void testHitMineReturnsDead() {
        // Player at (1,0), Mine at (1,1)
        final String board = "3\n3\n-1\n...\nPM.\n...\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Dead.class, result);
        final Dead dead = (Dead) result;
        // Player position should be reverted to original
        assertEquals(new Position(1, 0), dead.getOrigPosition());
        assertEquals(new Position(1, 0), dead.getNewPosition());
        // Player should be back at starting position
        assertEquals(new Position(1, 0), state.getGameBoard().getPlayerPosition());
    }

    @Test
    public void testSlideCollectMultipleGemsThenStopAtStopCell() {
        // Player at (0,0), Gem at (0,1), StopCell at (0,2)
        final String board = "1\n4\n-1\nPGS.\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;

        assertEquals(1, alive.getCollectedGems().size());
        // Player stops exactly on StopCell at (0,2)
        assertEquals(new Position(0, 2), alive.getNewPosition());
    }

    @Test
    public void testMultipleGemsSlidingPath() {
        // Player at (0,0), Gem at (0,1), empty at (0,2), Gem at (0,3), wall at (0,4)
        final String board = "3\n5\n-1\nPG.GW\n.....\n.....\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;

        // Should have collected both gems and stopped before wall at (0,3)
        assertEquals(2, alive.getCollectedGems().size());
        assertEquals(new Position(0, 3), alive.getNewPosition());
    }

    @Test
    public void testSlideSlideThenMine() {
        // Player at (0,0), Gem at (0,1), Mine at (0,2), Wall at (0,3)
        final String board = "3\n4\n-1\nPGMW\n....\n....\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Dead.class, result);

        // Player should be reverted to original position
        assertEquals(new Position(0, 0), state.getGameBoard().getPlayerPosition());
        // Gem should still be collected (removed)
        final Cell cell = state.getGameBoard().getCell(0, 1);
        assertNull(((EntityCell) cell).getEntity());
    }

    @Test
    public void testUndoAliveMove() {
        // Player at (1,0), Gem at (1,1), wall at (1,3)
        final String board = "3\n4\n-1\n....\nPG.W\n....\n";
        final GameState state = loadGameState(board);
        final GameState originalState = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);

        // Undo the move
        controller.undoMove((Alive) result);

        // Player should be back at starting position
        assertEquals(new Position(1, 0), state.getGameBoard().getPlayerPosition());
        // Gem should be restored
        final Cell cell = state.getGameBoard().getCell(1, 1);
        assertInstanceOf(Gem.class, ((EntityCell) cell).getEntity());
    }

    @Test
    public void testEmptyCellSlidingContinues() {
        // Player at (0,0), empty at (0,1), empty at (0,2), stopcell at (0,3)
        final String board = "1\n4\n-1\nP..S\n";
        final GameState state = loadGameState(board);
        final GameBoardController controller = state.getGameBoardController();

        final MoveResult result = controller.makeMove(Direction.RIGHT);
        assertInstanceOf(Alive.class, result);
        final Alive alive = (Alive) result;
        assertEquals(new Position(0, 3), alive.getNewPosition());
    }
}
