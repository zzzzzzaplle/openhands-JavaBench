package assignment.game;

/**
 * An action representing invalid user input.
 */
public class InvalidInput extends Action {
    private final String message;

    /**
     * Create an invalid input action.
     *
     * @param initiator the player ID who entered invalid input, or -1
     * @param message   a description of the invalid input
     */
    public InvalidInput(int initiator, String message) {
        super(initiator);
        this.message = message;
    }

    /**
     * @return the message describing the invalid input
     */
    public String getMessage() {
        return message;
    }
}
