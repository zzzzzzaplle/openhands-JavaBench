/**
 * Action representing an invalid input command from the user.
 */
public class InvalidInput extends Action {

    private final String message;

    /**
     * Creates an InvalidInput action with a descriptive message.
     *
     * @param initiator the player ID (typically -1 for system)
     * @param message   description of the invalid input
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
