import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GameStateSerializer}.
 */
public class GameStateSerializerTest {

    @Test
    public void testLoadFromReader() {
        final String board = "2\n3\n3\nP..\n.G.\n";
        try {
            final GameState state = GameStateSerializer.loadFrom(
                    new BufferedReader(new StringReader(board)));
            assertNotNull(state);
            assertEquals(2, state.getGameBoard().getNumRows());
            assertEquals(3, state.getGameBoard().getNumCols());
            assertEquals(3, state.getNumLives());
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    public void testLoadFromReaderUnlimitedLives() {
        final String board = "2\n3\n\nP..\n...\n";
        try {
            final GameState state = GameStateSerializer.loadFrom(
                    new BufferedReader(new StringReader(board)));
            assertNotNull(state);
            assertTrue(state.hasUnlimitedLives());
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    public void testLoadFromReaderWithAllCellTypes() {
        final String board = "3\n5\n-1\nW.L.G\nP..M.\n...S.\n";
        try {
            final GameState state = GameStateSerializer.loadFrom(
                    new BufferedReader(new StringReader(board)));
            assertNotNull(state);

            // Wall
            assertInstanceOf(Wall.class, state.getGameBoard().getCell(0, 0));

            // Entity cell with ExtraLife
            final Cell cell0_2 = state.getGameBoard().getCell(0, 2);
            assertInstanceOf(EntityCell.class, cell0_2);
            assertInstanceOf(ExtraLife.class, ((EntityCell) cell0_2).getEntity());

            // Entity cell with Gem
            final Cell cell0_4 = state.getGameBoard().getCell(0, 4);
            assertInstanceOf(EntityCell.class, cell0_4);
            assertInstanceOf(Gem.class, ((EntityCell) cell0_4).getEntity());

            // Player in StopCell
            final Cell cell1_0 = state.getGameBoard().getCell(1, 0);
            assertInstanceOf(StopCell.class, cell1_0);
            assertInstanceOf(Player.class, ((StopCell) cell1_0).getEntity());

            // Mine
            final Cell cell1_3 = state.getGameBoard().getCell(1, 3);
            assertInstanceOf(EntityCell.class, cell1_3);
            assertInstanceOf(Mine.class, ((EntityCell) cell1_3).getEntity());

            // StopCell without entity
            final Cell cell2_3 = state.getGameBoard().getCell(2, 3);
            assertInstanceOf(StopCell.class, cell2_3);
            assertNull(((StopCell) cell2_3).getEntity());
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    public void testWriteToAndLoadFrom(@TempDir final Path tempDir) {
        final String boardData = "2\n3\n3\nP..\n.G.\n";
        try {
            final GameState original = GameStateSerializer.loadFrom(
                    new BufferedReader(new StringReader(boardData)));

            final Path outputFile = tempDir.resolve("test_board.txt");
            GameStateSerializer.writeTo(original, outputFile);

            assertTrue(Files.exists(outputFile));
            final String content = Files.readString(outputFile);
            assertTrue(content.contains("P"));
            assertTrue(content.contains("G"));
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    public void testLoadUnknownCellThrows() {
        final String board = "1\n1\n-1\nZ\n";
        assertThrows(IllegalArgumentException.class, () -> {
            GameStateSerializer.loadFrom(new BufferedReader(new StringReader(board)));
        });
    }
}
