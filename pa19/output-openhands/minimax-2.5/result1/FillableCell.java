package game.map;

import java.util.Optional;

/**
 * A cell that can hold a pipe.
 */
public class FillableCell extends Cell {

    private Pipe pipe;

    public FillableCell(Coordinate coord) {
        super(coord);
    }

    /**
     * Gets the pipe in this cell.
     *
     * @return Optional containing the pipe, or empty if no pipe.
     */
    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    /**
     * Sets the pipe in this cell.
     *
     * @param pipe The pipe to set.
     */
    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    /**
     * Returns the character representation of this cell.
     *
     * @return '.' if no pipe, otherwise the pipe character.
     */
    @Override
    public char toSingleChar() {
        if (pipe != null) {
            return pipe.toSingleChar();
        }
        return '.';
    }
}