/**
 * Lightweight string helper utilities.
 */
public final class StringUtils {

    private StringUtils() {
        // non-instantiable
    }

    /**
     * Creates a string by repeating a character a given number of times.
     *
     * @param count Number of repetitions
     * @param ch    Character to repeat
     * @return String of {@code count} {@code ch} characters
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public static String createPadding(int count, char ch) {
        return String.valueOf(ch).repeat(count);
    }
}
