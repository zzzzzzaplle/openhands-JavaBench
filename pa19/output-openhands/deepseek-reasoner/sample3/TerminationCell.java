/**
 * A cell at which water enters (SOURCE) or exits (SINK) the map.
 */
public class TerminationCell extends Cell {

    private boolean isFilled;
    public final Direction pointingTo;
    public final TerminationType type;

    public TerminationCell(Coordinate coord, Direction pointingTo, TerminationType type) {
        super(coord);
        this.pointingTo = pointingTo;
        this.type = type;
    }

    public void setFilled() {
        isFilled = true;
    }

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
            }
        }
        throw new IllegalStateException("Unknown pointingTo: " + pointingTo);
    }
}
