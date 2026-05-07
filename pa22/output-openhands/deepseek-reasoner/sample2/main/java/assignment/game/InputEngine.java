package assignment.game;

/**
 * Interface for fetching player actions from an input source.
 */
public interface InputEngine {

    /**
     * Reads and returns the next action from the input source.
     *
     * @return the parsed Action
     */
    Action fetchAction();
}
