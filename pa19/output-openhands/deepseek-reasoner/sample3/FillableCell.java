import java.util.Optional;

/**
 * A cell that can hold an optional pipe.
 */
public class FillableCell extends Cell {

    private Pipe pipe;
    private boolean filled;

    public FillableCell(Coordinate coord) {
        super(coord);
    }

    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public char toSingleChar() {
        if (pipe == null) {
            return '.';
        }
        return pipe.toSingleChar(filled);
    }
}
