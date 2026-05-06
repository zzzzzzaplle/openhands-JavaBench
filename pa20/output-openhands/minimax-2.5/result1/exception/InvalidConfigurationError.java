package assignment.protocol.exception;

/**
 * Exception thrown when the game configuration is invalid.
 */
public class InvalidConfigurationError extends RuntimeException {
    public InvalidConfigurationError(String message) {
        super(message);
    }
}