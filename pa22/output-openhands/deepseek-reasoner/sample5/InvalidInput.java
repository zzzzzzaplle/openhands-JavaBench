/**
 * Action representing invalid user input.
 */
public class InvalidInput extends Action {

    private final String message;

    /**
     * Create a new invalid input action.
     *
     * @param initiator the initiator ID (typically -1 for system)
     * @param message   a description of the invalid input
     */
    public InvalidInput(int initiator, String message) {
        super(initiator);
        this.message = message;
    }

    /**
     * @return the error message describing the invalid input
     */
    public String getMessage() {
        return message;
    }
}
