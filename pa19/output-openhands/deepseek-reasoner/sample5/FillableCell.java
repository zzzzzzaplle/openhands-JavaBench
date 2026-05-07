import java.util.Optional;

/**
 * A cell that can hold a pipe.
 */
public class FillableCell extends Cell {

    private Pipe pipe;

    public FillableCell(Coordinate coord) {
        super(coord);
        this.pipe = null;
    }

    /**
     * Returns the pipe in this cell, if present.
     *
     * @return Optional containing the pipe, or empty
     */
    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    /**
     * Sets the pipe in this cell.
     *
     * @param pipe Pipe to place
     */
    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public char toSingleChar() {
        if (pipe != null) {
            return pipe.toSingleChar();
        }
        return '.';
    }
}
