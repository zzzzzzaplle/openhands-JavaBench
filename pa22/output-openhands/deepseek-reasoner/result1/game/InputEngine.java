package assignment.game;

/**
 * Interface for reading user input and converting it into game actions.
 */
public interface InputEngine {
    /**
     * Read the next action from the user.
     *
     * @return the parsed action
     */
    Action fetchAction();
}
