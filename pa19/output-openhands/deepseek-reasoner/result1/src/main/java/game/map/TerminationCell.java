package game.map;

import game.Direction;
import game.TerminationType;
import game.util.PipePatterns;

/**
 * A cell that serves as a SOURCE or SINK termination point.
 */
public class TerminationCell extends Cell {

    public final Direction pointingTo;
    public final TerminationType type;
    private boolean isFilled;

    public TerminationCell(Coordinate coord, Direction pointingTo, TerminationType type) {
        super(coord);
        this.pointingTo = pointingTo;
        this.type = type;
        this.isFilled = false;
    }

    /**
     * Marks this cell as filled with water.
     */
    public void setFilled() {
        this.isFilled = true;
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
     * Returns the appropriate arrow character based on fill state.
     * Both SOURCE and SINK use pointingTo directly without direction reversal.
     *
     * @return arrow character
     */
    @Override
    public char toSingleChar() {
        if (isFilled) {
            switch (pointingTo) {
                case UP:
                    return PipePatterns.Filled.UP_ARROW;
                case DOWN:
                    return PipePatterns.Filled.DOWN_ARROW;
                case LEFT:
                    return PipePatterns.Filled.LEFT_ARROW;
                case RIGHT:
                    return PipePatterns.Filled.RIGHT_ARROW;
                default:
                    throw new IllegalStateException("Unexpected direction: " + pointingTo);
            }
        } else {
            switch (pointingTo) {
                case UP:
                    return PipePatterns.Unfilled.UP_ARROW;
                case DOWN:
                    return PipePatterns.Unfilled.DOWN_ARROW;
                case LEFT:
                    return PipePatterns.Unfilled.LEFT_ARROW;
                case RIGHT:
                    return PipePatterns.Unfilled.RIGHT_ARROW;
                default:
                    throw new IllegalStateException("Unexpected direction: " + pointingTo);
            }
        }
    }
}
