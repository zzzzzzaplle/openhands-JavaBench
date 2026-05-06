package game.map;

/**
 * Enumeration of pipe shapes with their rendering characters.
 */
public enum PipeShape {
    HORIZONTAL(PipePatterns.Filled.HORIZONTAL, PipePatterns.Unfilled.HORIZONTAL),
    VERTICAL(PipePatterns.Filled.VERTICAL, PipePatterns.Unfilled.VERTICAL),
    TOP_LEFT(PipePatterns.Filled.TOP_LEFT, PipePatterns.Unfilled.TOP_LEFT),
    TOP_RIGHT(PipePatterns.Filled.TOP_RIGHT, PipePatterns.Unfilled.TOP_RIGHT),
    BOTTOM_LEFT(PipePatterns.Filled.BOTTOM_LEFT, PipePatterns.Unfilled.BOTTOM_LEFT),
    BOTTOM_RIGHT(PipePatterns.Filled.BOTTOM_RIGHT, PipePatterns.Unfilled.BOTTOM_RIGHT),
    CROSS(PipePatterns.Filled.CROSS, PipePatterns.Unfilled.CROSS);

    private final char filledChar;
    private final char unfilledChar;

    PipeShape(char filled, char unfilled) {
        this.filledChar = filled;
        this.unfilledChar = unfilled;
    }

    /**
     * Returns the character for this pipe based on filled state.
     *
     * @param isFilled True if filled.
     * @return The character representation.
     */
    public char getCharByState(boolean isFilled) {
        return isFilled ? filledChar : unfilledChar;
    }

    /**
     * Returns the list of connection directions for this pipe shape.
     *
     * @return Array of directions.
     */
    public Direction[] getConnections() {
        return switch (this) {
            case HORIZONTAL -> new Direction[]{Direction.LEFT, Direction.RIGHT};
            case VERTICAL -> new Direction[]{Direction.UP, Direction.DOWN};
            case TOP_LEFT -> new Direction[]{Direction.UP, Direction.LEFT};
            case TOP_RIGHT -> new Direction[]{Direction.UP, Direction.RIGHT};
            case BOTTOM_LEFT -> new Direction[]{Direction.DOWN, Direction.LEFT};
            case BOTTOM_RIGHT -> new Direction[]{Direction.DOWN, Direction.RIGHT};
            case CROSS -> new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        };
    }

    /**
     * Parses a short string code to get the pipe shape.
     *
     * @param code The short code (HZ, VT, TL, TR, BL, BR, CR).
     * @return The corresponding PipeShape.
     */
    public static PipeShape fromString(String code) {
        return switch (code.toUpperCase()) {
            case "HZ" -> HORIZONTAL;
            case "VT" -> VERTICAL;
            case "TL" -> TOP_LEFT;
            case "TR" -> TOP_RIGHT;
            case "BL" -> BOTTOM_LEFT;
            case "BR" -> BOTTOM_RIGHT;
            case "CR" -> CROSS;
            default -> throw new IllegalArgumentException("Unknown pipe shape code: " + code);
        };
    }
}