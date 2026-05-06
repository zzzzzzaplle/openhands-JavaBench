package game;

/**
 * Interface for input engine that fetches actions.
 */
public interface InputEngine {
    /**
     * Fetch the next action from the user.
     */
    Action fetchAction();
}