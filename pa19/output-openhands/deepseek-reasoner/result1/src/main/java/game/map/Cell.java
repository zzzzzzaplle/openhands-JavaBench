package game.map;

import game.Direction;
import game.TerminationType;
import game.pipe.Pipe;
import game.pipe.PipeShape;
import game.util.PipePatterns;

/**
 * Abstract base class for all cells on the map.
 */
public abstract class Cell implements MapElement {

    public final Coordinate coord;

    protected Cell(Coordinate coord) {
        this.coord = coord;
    }

    /**
     * Parses a character and creates the corresponding Cell.
     *
     * @param c                character to parse
     * @param coord            coordinate of the cell
     * @param terminationType  termination type (SOURCE or SINK) for termination cells
     * @return the created Cell
     * @throws IllegalArgumentException if the character is unknown
     */
    public static Cell fromChar(char c, Coordinate coord, TerminationType terminationType) {
        switch (c) {
            case PipePatterns.WALL:
                return new Wall(coord);
            case PipePatterns.Unfilled.HORIZONTAL:
            case PipePatterns.Filled.HORIZONTAL:
                return new FillableCell(coord, new Pipe(PipeShape.HORIZONTAL));
            case PipePatterns.Unfilled.VERTICAL:
            case PipePatterns.Filled.VERTICAL:
                return new FillableCell(coord, new Pipe(PipeShape.VERTICAL));
            case PipePatterns.Unfilled.TOP_LEFT:
            case PipePatterns.Filled.TOP_LEFT:
                return new FillableCell(coord, new Pipe(PipeShape.TOP_LEFT));
            case PipePatterns.Unfilled.TOP_RIGHT:
            case PipePatterns.Filled.TOP_RIGHT:
                return new FillableCell(coord, new Pipe(PipeShape.TOP_RIGHT));
            case PipePatterns.Unfilled.BOTTOM_LEFT:
            case PipePatterns.Filled.BOTTOM_LEFT:
                return new FillableCell(coord, new Pipe(PipeShape.BOTTOM_LEFT));
            case PipePatterns.Unfilled.BOTTOM_RIGHT:
            case PipePatterns.Filled.BOTTOM_RIGHT:
                return new FillableCell(coord, new Pipe(PipeShape.BOTTOM_RIGHT));
            case PipePatterns.Unfilled.CROSS:
            case PipePatterns.Filled.CROSS:
                return new FillableCell(coord, new Pipe(PipeShape.CROSS));
            case PipePatterns.Unfilled.UP_ARROW:
            case PipePatterns.Filled.UP_ARROW:
                return new TerminationCell(coord, Direction.UP, terminationType);
            case PipePatterns.Unfilled.DOWN_ARROW:
            case PipePatterns.Filled.DOWN_ARROW:
                return new TerminationCell(coord, Direction.DOWN, terminationType);
            case PipePatterns.Unfilled.LEFT_ARROW:
            case PipePatterns.Filled.LEFT_ARROW:
                return new TerminationCell(coord, Direction.LEFT, terminationType);
            case PipePatterns.Unfilled.RIGHT_ARROW:
            case PipePatterns.Filled.RIGHT_ARROW:
                return new TerminationCell(coord, Direction.RIGHT, terminationType);
            case '.':
            case ' ':
                return new FillableCell(coord, null);
            default:
                throw new IllegalArgumentException("Unknown cell character: " + c);
        }
    }
}
