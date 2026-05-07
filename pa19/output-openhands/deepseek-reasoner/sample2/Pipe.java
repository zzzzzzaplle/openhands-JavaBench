/**
 * Represents a pipe piece with a specific shape that determines its connection directions.
 */
public class Pipe implements MapElement {

    private final PipeShape shape;
    private boolean filled;

    public Pipe(PipeShape shape) {
        this.shape = shape;
        this.filled = false;
    }

    /**
     * Constructs a pipe from a short string code.
     *
     * @param code the short code (HZ, VT, TL, TR, BL, BR, CR)
     * @return the constructed Pipe
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

    public PipeShape getShape() {
        return shape;
    }

    /**
     * Marks this pipe as filled with water.
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    /**
     * Returns the connection directions based on this pipe's shape.
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
                throw new IllegalStateException("Unexpected shape: " + shape);
        }
    }

    @Override
    public char toSingleChar() {
        return shape.getCharByState(filled);
    }
}
