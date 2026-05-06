package game.pipe;

import game.Direction;
import game.map.MapElement;

/**
 * A pipe piece with a specific shape that can be placed on the map.
 */
public class Pipe implements MapElement {

    private final PipeShape shape;

    /**
     * Constructs a pipe from a short string code.
     *
     * @param code string code: HZ, VT, TL, TR, BL, BR, CR
     */
    public Pipe(String code) {
        this.shape = parseShape(code);
    }

    /**
     * Constructs a pipe with the given shape.
     *
     * @param shape pipe shape
     */
    public Pipe(PipeShape shape) {
        this.shape = shape;
    }

    private static PipeShape parseShape(String code) {
        switch (code.toUpperCase()) {
            case "HZ":
                return PipeShape.HORIZONTAL;
            case "VT":
                return PipeShape.VERTICAL;
            case "TL":
                return PipeShape.TOP_LEFT;
            case "TR":
                return PipeShape.TOP_RIGHT;
            case "BL":
                return PipeShape.BOTTOM_LEFT;
            case "BR":
                return PipeShape.BOTTOM_RIGHT;
            case "CR":
                return PipeShape.CROSS;
            default:
                throw new IllegalArgumentException("Unknown pipe code: " + code);
        }
    }

    /**
     * Returns the shape of this pipe.
     *
     * @return pipe shape
     */
    public PipeShape getShape() {
        return shape;
    }

    /**
     * Returns the connection directions based on the pipe shape.
     *
     * @return array of connection directions
     */
    public Direction[] getConnections() {
        return shape.getConnections();
    }

    /**
     * Returns the character representation for this pipe (unfilled by default).
     *
     * @return rendering character
     */
    @Override
    public char toSingleChar() {
        return shape.getCharByState(false);
    }

    /**
     * Returns the character representation based on fill state.
     *
     * @param filled whether the pipe is filled with water
     * @return rendering character
     */
    public char toSingleChar(boolean filled) {
        return shape.getCharByState(filled);
    }
}
