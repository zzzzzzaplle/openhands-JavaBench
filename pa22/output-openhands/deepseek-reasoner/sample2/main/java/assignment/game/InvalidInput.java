package assignment.game;

/**
 * Represents an action resulting from invalid user input.
 */
public class InvalidInput extends Action {

    private final String message;

    /**
     * Creates an InvalidInput action with a descriptive message.
     *
     * @param initiator the player ID who initiated the action, or -1
     * @param message   a description of the invalid input
     */
    public InvalidInput(int initiator, String message) {
        super(initiator);
        this.message = message;
    }

    /**
     * Returns the message describing the invalid input.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
