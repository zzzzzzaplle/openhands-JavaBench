package game.util;

/**
 * Lightweight string helper utilities.
 */
public final class StringUtils {

    private StringUtils() {
        // prevent instantiation
    }

    /**
     * Creates a string consisting of {@code ch} repeated {@code count} times.
     *
     * @param count repeat count
     * @param ch    character to repeat
     * @return string of repeated characters
     */
    public static String createPadding(int count, char ch) {
        return String.valueOf(ch).repeat(count);
    }
}
