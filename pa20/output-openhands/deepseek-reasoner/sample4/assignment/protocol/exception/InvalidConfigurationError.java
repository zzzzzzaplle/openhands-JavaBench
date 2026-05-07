package assignment.protocol.exception;

/**
 * Custom error class for invalid configuration parameters.
 */
public class InvalidConfigurationError extends Error {
    public InvalidConfigurationError(String message) {
        super(message);
    }

    public InvalidConfigurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
