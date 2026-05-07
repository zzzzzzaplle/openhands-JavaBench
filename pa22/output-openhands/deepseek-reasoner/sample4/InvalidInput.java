/**
 * Represents an action resulting from invalid user input.
 */
public class InvalidInput extends Action {

    private final String message;

    /**
     * Create an invalid input action.
     *
     * @param initiator the player ID or -1 for system actions
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
