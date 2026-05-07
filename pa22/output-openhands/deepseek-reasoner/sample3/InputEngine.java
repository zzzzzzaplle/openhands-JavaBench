/**
 * Interface for reading player input and converting it to game actions.
 */
public interface InputEngine {

    /**
     * Reads and returns the next action from the input source.
     *
     * @return the parsed Action
     */
    Action fetchAction();
}
