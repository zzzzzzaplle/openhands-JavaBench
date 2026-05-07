/**
 * Interface for reading user input and converting it to game actions.
 */
public interface InputEngine {

    /**
     * Fetch the next action from user input.
     *
     * @return the parsed Action
     */
    Action fetchAction();
}
