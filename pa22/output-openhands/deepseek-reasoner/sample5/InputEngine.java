/**
 * Interface for reading user input actions.
 */
public interface InputEngine {

    /**
     * Read and return the next action from the user.
     *
     * @return the action corresponding to the user input
     */
    Action fetchAction();
}
