package game;

import game.map.*;
import game.pipe.*;
import game.util.*;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Basic verification tests for the pipe-connection game.
 */
public class GameTest {

    @Test
    public void testCoordinate() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        Coordinate c3 = new Coordinate(2, 1);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertEquals(3, c1.add(new Coordinate(2, 1)).row);
        assertEquals(3, c1.add(new Coordinate(2, 1)).col);
    }

    @Test
    public void testDirection() {
        assertEquals(Direction.DOWN, Direction.UP.getOpposite());
        assertEquals(Direction.UP, Direction.DOWN.getOpposite());
        assertEquals(Direction.RIGHT, Direction.LEFT.getOpposite());
        assertEquals(Direction.LEFT, Direction.RIGHT.getOpposite());

        Coordinate upOffset = Direction.UP.getOffset();
        assertEquals(-1, upOffset.row);
        assertEquals(0, upOffset.col);
    }

    @Test
    public void testPipeShape() {
        Pipe pipe = new Pipe("HZ");
        assertEquals(PipeShape.HORIZONTAL, pipe.getShape());
        assertArrayEquals(new Direction[]{Direction.LEFT, Direction.RIGHT}, pipe.getConnections());

        Pipe pipe2 = new Pipe("CR");
        assertEquals(PipeShape.CROSS, pipe2.getShape());
        assertArrayEquals(
            new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT},
            pipe2.getConnections()
        );
    }

    @Test
    public void testPipeToString() {
        Pipe pipe = new Pipe(PipeShape.VERTICAL);
        assertEquals(PipePatterns.Unfilled.VERTICAL, pipe.toSingleChar());
        assertEquals(PipePatterns.Filled.VERTICAL, pipe.toSingleChar(true));
    }

    @Test
    public void testTerminationCell() {
        Coordinate coord = new Coordinate(2, 2);
        TerminationCell source = new TerminationCell(coord, Direction.DOWN, TerminationType.SOURCE);
        assertFalse(source.isFilled());
        assertEquals(PipePatterns.Unfilled.DOWN_ARROW, source.toSingleChar());

        source.setFilled();
        assertTrue(source.isFilled());
        assertEquals(PipePatterns.Filled.DOWN_ARROW, source.toSingleChar());
    }

    @Test
    public void testFillableCell() {
        Coordinate coord = new Coordinate(1, 1);
        FillableCell cell = new FillableCell(coord, null);
        assertFalse(cell.getPipe().isPresent());
        assertEquals('.', cell.toSingleChar());

        Pipe pipe = new Pipe(PipeShape.HORIZONTAL);
        cell.setPipe(pipe);
        assertTrue(cell.getPipe().isPresent());
        assertEquals(PipePatterns.Unfilled.HORIZONTAL, cell.toSingleChar());

        cell.setFilled();
        assertEquals(PipePatterns.Filled.HORIZONTAL, cell.toSingleChar());
    }

    @Test
    public void testWall() {
        Wall wall = new Wall(new Coordinate(0, 0));
        assertEquals(PipePatterns.WALL, wall.toSingleChar());
    }

    @Test
    public void testCellFromChar() {
        Cell wall = Cell.fromChar('#', new Coordinate(0, 0), TerminationType.SINK);
        assertTrue(wall instanceof Wall);

        Cell fillable = Cell.fromChar('.', new Coordinate(1, 1), TerminationType.SOURCE);
        assertTrue(fillable instanceof FillableCell);

        Cell sourceDown = Cell.fromChar('\u2193', new Coordinate(2, 2), TerminationType.SOURCE);
        assertTrue(sourceDown instanceof TerminationCell);
        assertEquals(Direction.DOWN, ((TerminationCell) sourceDown).pointingTo);
        assertEquals(TerminationType.SOURCE, ((TerminationCell) sourceDown).type);
    }

    @Test
    public void testPipeQueue() {
        Pipe p1 = new Pipe(PipeShape.HORIZONTAL);
        Pipe p2 = new Pipe(PipeShape.VERTICAL);
        PipeQueue queue = new PipeQueue(List.of(p1, p2));

        assertEquals(p1, queue.peek());
        queue.consume();
        assertNotEquals(p1, queue.peek());
    }

    @Test
    public void testCellStack() {
        CellStack stack = new CellStack();
        assertTrue(stack.isEmpty());

        FillableCell cell = new FillableCell(new Coordinate(1, 1), new Pipe(PipeShape.CROSS));
        stack.push(cell);
        assertFalse(stack.isEmpty());

        FillableCell popped = stack.pop();
        assertEquals(cell, popped);
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testDelayBar() {
        DelayBar bar = new DelayBar(3);
        assertEquals(-3, bar.distance());

        bar.countdown();
        assertEquals(-2, bar.distance());

        bar.countdown();
        assertEquals(-1, bar.distance());

        bar.countdown();
        assertEquals(0, bar.distance());

        bar.countdown();
        assertEquals(1, bar.distance());
    }

    @Test
    public void testStringUtils() {
        assertEquals("===", StringUtils.createPadding(3, '='));
        assertEquals("", StringUtils.createPadding(0, ' '));
    }

    @Test
    public void testMapCreationAndPlacement() {
        Cell[][] cells = new Cell[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cells[r][c] = new Wall(new Coordinate(r, c));
            }
        }
        for (int r = 1; r < 4; r++) {
            for (int c = 1; c < 4; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c), null);
            }
        }
        cells[2][2] = new TerminationCell(new Coordinate(2, 2), Direction.DOWN, TerminationType.SOURCE);
        // SINK at (4,3) on bottom edge, pointing DOWN (outward)
        cells[4][3] = new TerminationCell(new Coordinate(4, 3), Direction.DOWN, TerminationType.SINK);

        Map map = new Map(5, 5, cells);

        assertTrue(map.tryPlacePipe(1, 1, new Pipe(PipeShape.HORIZONTAL)));
        assertFalse(map.tryPlacePipe(0, 0, new Pipe(PipeShape.HORIZONTAL)));
        assertFalse(map.tryPlacePipe(2, 2, new Pipe(PipeShape.HORIZONTAL)));
        assertFalse(map.tryPlacePipe(4, 3, new Pipe(PipeShape.HORIZONTAL)));
        assertFalse(map.tryPlacePipe(1, 1, new Pipe(PipeShape.VERTICAL)));

        map.undo(new Coordinate(1, 1));
        assertTrue(map.tryPlacePipe(1, 1, new Pipe(PipeShape.VERTICAL)));
    }

    @Test
    public void testWaterFlowAndWinLoss() {
        Cell[][] cells = new Cell[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cells[r][c] = new Wall(new Coordinate(r, c));
            }
        }
        for (int r = 1; r < 4; r++) {
            for (int c = 1; c < 4; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c), null);
            }
        }
        cells[1][2] = new TerminationCell(new Coordinate(1, 2), Direction.DOWN, TerminationType.SOURCE);
        cells[4][2] = new TerminationCell(new Coordinate(4, 2), Direction.DOWN, TerminationType.SINK);

        Map map = new Map(5, 5, cells);

        assertTrue(map.tryPlacePipe(2, 2, new Pipe(PipeShape.VERTICAL)));
        assertTrue(map.tryPlacePipe(3, 2, new Pipe(PipeShape.VERTICAL)));

        map.fillBeginTile();
        map.fillTiles(1);
        map.fillTiles(2);
        FillableCell cell22 = (FillableCell) map.getCell(2, 2);
        assertTrue("Pipe at (2,2) should be filled", cell22.isFilled());

        map.fillTiles(3);
        FillableCell cell32 = (FillableCell) map.getCell(3, 2);
        assertTrue("Pipe at (3,2) should be filled", cell32.isFilled());

        // After all pipes filled, hasLost() may return true (no more new tiles).
        // Game loop checks win before loss, so only verify path exists.
        assertTrue("Path from source to sink should exist", map.checkPath());
    }

    @Test
    public void testHasLostDuringFlow() {
        // Test that hasLost() returns false while water is still making progress
        Cell[][] cells = new Cell[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cells[r][c] = new Wall(new Coordinate(r, c));
            }
        }
        for (int r = 1; r < 4; r++) {
            for (int c = 1; c < 4; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c), null);
            }
        }
        cells[1][2] = new TerminationCell(new Coordinate(1, 2), Direction.DOWN, TerminationType.SOURCE);
        cells[4][2] = new TerminationCell(new Coordinate(4, 2), Direction.DOWN, TerminationType.SINK);

        Map map = new Map(5, 5, cells);
        map.tryPlacePipe(2, 2, new Pipe(PipeShape.VERTICAL));
        map.tryPlacePipe(3, 2, new Pipe(PipeShape.VERTICAL));

        map.fillBeginTile();
        map.fillTiles(1);
        // After distance 1, one new pipe was filled (at row 2)
        assertFalse("Should not have lost while water is progressing", map.hasLost());

        map.fillTiles(2);
        // After distance 2, another new pipe was filled (at row 3)
        assertFalse("Should not have lost while water is progressing", map.hasLost());
    }

    @Test
    public void testGameBasicFlow() {
        Cell[][] cells = new Cell[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cells[r][c] = new Wall(new Coordinate(r, c));
            }
        }
        for (int r = 1; r < 4; r++) {
            for (int c = 1; c < 4; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c), null);
            }
        }
        cells[1][2] = new TerminationCell(new Coordinate(1, 2), Direction.DOWN, TerminationType.SOURCE);
        cells[4][2] = new TerminationCell(new Coordinate(4, 2), Direction.DOWN, TerminationType.SINK);

        Pipe p1 = new Pipe(PipeShape.VERTICAL);
        Pipe p2 = new Pipe(PipeShape.VERTICAL);
        Game game = new Game(5, 5, 2, cells, List.of(p1, p2));

        assertTrue(game.placePipe(2, 2, new Pipe(PipeShape.VERTICAL)));
        assertTrue(game.placePipe(3, 2, new Pipe(PipeShape.VERTICAL)));

        game.advanceWaterFlow();
        game.advanceWaterFlow();
        game.advanceWaterFlow();
        game.advanceWaterFlow();

        assertTrue("Should have a path", game.checkWin());
    }

    @Test
    public void testSkipAndUndo() {
        Cell[][] cells = new Cell[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cells[r][c] = new Wall(new Coordinate(r, c));
            }
        }
        for (int r = 1; r < 4; r++) {
            for (int c = 1; c < 4; c++) {
                cells[r][c] = new FillableCell(new Coordinate(r, c), null);
            }
        }
        cells[1][2] = new TerminationCell(new Coordinate(1, 2), Direction.DOWN, TerminationType.SOURCE);
        cells[4][2] = new TerminationCell(new Coordinate(4, 2), Direction.DOWN, TerminationType.SINK);

        Game game = new Game(5, 5, 2, cells, List.of());
        assertEquals(0, game.getSteps());

        assertTrue(game.placePipe(2, 2, new Pipe(PipeShape.VERTICAL)));
        assertEquals(1, game.getSteps());

        game.skipPipe();
        assertEquals(2, game.getSteps());

        assertTrue(game.undoStep());
        assertEquals(3, game.getSteps());
    }
}
