/**
 * A cell representing a water source or sink on the map border or interior.
 */
public class TerminationCell extends Cell {

    private boolean isFilled;
    public final Direction pointingTo;
    public final TerminationType type;

    public TerminationCell(Coordinate coord, Direction pointingTo, TerminationType type) {
        super(coord);
        this.pointingTo = pointingTo;
        this.type = type;
        this.isFilled = false;
    }

    /**
     * Marks this termination cell as filled with water.
     */
    public void setFilled() {
        this.isFilled = true;
    }

    /**
     * Returns whether this cell is currently filled.
     */
    public boolean isFilled() {
        return isFilled;
    }

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
                    return '?';
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
                    return '?';
            }
        }
    }
}
