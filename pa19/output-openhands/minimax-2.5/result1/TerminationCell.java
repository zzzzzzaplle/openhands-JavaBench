package game.map;

/**
 * A termination cell (SOURCE or SINK) that has a direction it points to.
 */
public class TerminationCell extends Cell {

    private boolean isFilled;
    private final Direction pointingTo;
    private final TerminationType type;

    public TerminationCell(Coordinate coord, Direction pointingTo, TerminationType type) {
        super(coord);
        this.pointingTo = pointingTo;
        this.type = type;
        this.isFilled = false;
    }

    /**
     * Sets this cell as filled.
     */
    public void setFilled() {
        this.isFilled = true;
    }

    /**
     * Returns the character representation of this cell.
     * When filled, returns the arrow from PipePatterns.Filled.
     * When unfilled, returns the arrow from PipePatterns.Unfilled.
     *
     * @return The arrow character based on state and pointing direction.
     */
    @Override
    public char toSingleChar() {
        return isFilled ? getFilledArrow() : getUnfilledArrow();
    }

    private char getFilledArrow() {
        return switch (pointingTo) {
            case UP -> PipePatterns.Filled.UP_ARROW;
            case DOWN -> PipePatterns.Filled.DOWN_ARROW;
            case LEFT -> PipePatterns.Filled.LEFT_ARROW;
            case RIGHT -> PipePatterns.Filled.RIGHT_ARROW;
        };
    }

    private char getUnfilledArrow() {
        return switch (pointingTo) {
            case UP -> PipePatterns.Unfilled.UP_ARROW;
            case DOWN -> PipePatterns.Unfilled.DOWN_ARROW;
            case LEFT -> PipePatterns.Unfilled.LEFT_ARROW;
            case RIGHT -> PipePatterns.Unfilled.RIGHT_ARROW;
        };
    }

    /**
     * Gets the direction this termination points to.
     *
     * @return The direction.
     */
    public Direction getPointingTo() {
        return pointingTo;
    }

    /**
     * Gets the termination type.
     *
     * @return The type (SOURCE or SINK).
     */
    public TerminationType getType() {
        return type;
    }

    /**
     * Checks if this cell is filled.
     *
     * @return True if filled.
     */
    public boolean isFilled() {
        return isFilled;
    }
}