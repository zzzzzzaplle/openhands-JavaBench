package game.map;

import game.pipe.Pipe;

import java.util.Optional;

/**
 * A cell that can hold a pipe. Renders the pipe character or '.' if empty.
 */
public class FillableCell extends Cell {

    private Pipe pipe;
    private boolean isFilled;

    public FillableCell(Coordinate coord, Pipe pipe) {
        super(coord);
        this.pipe = pipe;
        this.isFilled = false;
    }

    /**
     * Returns the pipe held by this cell, if any.
     *
     * @return Optional containing the pipe, or empty
     */
    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    /**
     * Sets the pipe in this cell.
     *
     * @param pipe pipe to set
     */
    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    /**
     * Returns whether this cell is filled with water.
     *
     * @return true if filled
     */
    public boolean isFilled() {
        return isFilled;
    }

    /**
     * Marks this cell as filled with water.
     */
    public void setFilled() {
        this.isFilled = true;
    }

    @Override
    public char toSingleChar() {
        if (pipe == null) {
            return '.';
        }
        return pipe.toSingleChar(isFilled);
    }
}
