import java.util.Optional;

/**
 * A cell that can hold a pipe.
 */
public class FillableCell extends Cell {

    private Pipe pipe;
    boolean isFilled; // package-private for Map to set during water flow

    public FillableCell(Coordinate coord) {
        super(coord);
        this.pipe = null;
        this.isFilled = false;
    }

    /**
     * Returns the pipe held in this cell, if any.
     *
     * @return Optional containing the pipe, or empty
     */
    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    /**
     * Places a pipe in this cell.
     *
     * @param pipe the pipe to place
     */
    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    /**
     * Removes the pipe from this cell and resets filled state.
     */
    public void clearPipe() {
        this.pipe = null;
        this.isFilled = false;
    }

    @Override
    public char toSingleChar() {
        if (pipe != null) {
            return pipe.toSingleChar(isFilled);
        }
        return '.';
    }
}
