package game.map;

/**
 * Represents a pipe that can be placed in the map.
 */
public class Pipe implements MapElement {

    private final PipeShape shape;
    private boolean isFilled;

    public Pipe(PipeShape shape) {
        this.shape = shape;
        this.isFilled = false;
    }

    /**
     * Constructs a pipe from a short string code.
     *
     * @param code The code (HZ, VT, TL, TR, BL, BR, CR).
     */
    public Pipe(String code) {
        this(PipeShape.fromString(code));
    }

    /**
     * Gets the shape of this pipe.
     *
     * @return The pipe shape.
     */
    public PipeShape getShape() {
        return shape;
    }

    /**
     * Returns the connection directions for this pipe.
     *
     * @return Array of connection directions.
     */
    public Direction[] getConnections() {
        return shape.getConnections();
    }

    /**
     * Returns the character representation of this pipe.
     * Different for filled and unfilled pipes.
     *
     * @return The character.
     */
    @Override
    public char toSingleChar() {
        return shape.getCharByState(isFilled);
    }

    /**
     * Sets the filled state of this pipe.
     *
     * @param filled True if filled.
     */
    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    /**
     * Checks if this pipe is filled.
     *
     * @return True if filled.
     */
    public boolean isFilled() {
        return isFilled;
    }
}