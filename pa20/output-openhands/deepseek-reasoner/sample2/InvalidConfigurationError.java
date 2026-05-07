public class InvalidConfigurationError extends Error {
    public InvalidConfigurationError(String message) {
        super(message);
    }

    public InvalidConfigurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
