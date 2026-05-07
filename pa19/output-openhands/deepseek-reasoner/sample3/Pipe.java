/**
 * A pipe with a shape that determines its connection directions and rendering.
 */
public class Pipe {

    private final PipeShape shape;

    public Pipe(PipeShape shape) {
        this.shape = shape;
    }

    public PipeShape getShape() {
        return shape;
    }

    /**
     * Returns the connection directions based on the pipe shape.
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
            default:
                throw new IllegalStateException("Unknown shape: " + shape);
        }
    }

    /**
     * Returns the character representation, different for filled and unfilled pipes.
     */
    public char toSingleChar(boolean isFilled) {
        return shape.getCharByState(isFilled);
    }

    /**
     * Convenience: renders as unfilled.
     */
    public char toSingleChar() {
        return toSingleChar(false);
    }

    /**
     * Creates a pipe from a short string code.
     */
    public static Pipe fromCode(String code) {
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
                throw new IllegalArgumentException("Unknown pipe code: " + code);
        }
    }
}
