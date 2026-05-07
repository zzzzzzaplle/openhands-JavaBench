/**
 * Represents a pipe with a specific shape that can be placed on the map.
 */
public class Pipe implements MapElement {

    private final PipeShape shape;
    private boolean filled;

    public Pipe(PipeShape shape) {
        this.shape = shape;
        this.filled = false;
    }

    /**
     * Returns the shape of this pipe.
     *
     * @return PipeShape
     */
    public PipeShape getShape() {
        return shape;
    }

    /**
     * Marks this pipe as filled (or unfilled) with water.
     *
     * @param filled true to mark as filled
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    /**
     * Returns whether this pipe is filled with water.
     *
     * @return true if filled
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * Returns the connection directions based on this pipe's shape.
     *
     * @return Array of directions this pipe connects to
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
