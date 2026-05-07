import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A pipe that can be placed on the map, defined by its shape and connection directions.
 */
public class Pipe implements MapElement {

    private final PipeShape shape;

    public Pipe(PipeShape shape) {
        this.shape = shape;
    }

    /**
     * Constructs a pipe from a short string code.
     *
     * @param code short code: HZ, VT, TL, TR, BL, BR, CR
     * @return a new Pipe, or null if the code is unknown
     */
    public static Pipe fromString(String code) {
        if (code == null) {
            return null;
        }
        switch (code.toUpperCase()) {
            case "HZ":
                return new Pipe(PipeShape.HORIZONTAL);
            case "VT":
                return new Pipe(PipeShape.VERTICAL);
            case "TL":
                return new Pipe(PipeShape.TOP_LEFT);
            case "TR":
                return new Pipe(PipeShape.TOP_RIGHT);
            case "BL":
                return new Pipe(PipeShape.BOTTOM_LEFT);
            case "BR":
                return new Pipe(PipeShape.BOTTOM_RIGHT);
            case "CR":
                return new Pipe(PipeShape.CROSS);
            default:
                return null;
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
     * Returns the connection directions for this pipe based on its shape.
     *
     * @return array of connection directions
     */
    public Direction[] getConnections() {
        switch (shape) {
            case HORIZONTAL:
                return new Direction[]{Direction.LEFT, Direction.RIGHT};
            case VERTICAL:
                return new Direction[]{Direction.UP, Direction.DOWN};
            case TOP_LEFT:
                return new Direction[]{Direction.UP, Direction.LEFT};
            case TOP_RIGHT:
                return new Direction[]{Direction.UP, Direction.RIGHT};
            case BOTTOM_LEFT:
                return new Direction[]{Direction.DOWN, Direction.LEFT};
            case BOTTOM_RIGHT:
                return new Direction[]{Direction.DOWN, Direction.RIGHT};
            case CROSS:
                return new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        }
        throw new IllegalStateException("Unexpected shape: " + shape);
    }

    @Override
    public char toSingleChar() {
        // Default rendering: unfilled
        return shape.getCharByState(false);
    }

    /**
     * Returns the character representation based on fill state.
     *
     * @param isFilled whether the pipe is filled with water
     * @return character to render
     */
    public char toSingleChar(boolean isFilled) {
        return shape.getCharByState(isFilled);
    }
}
