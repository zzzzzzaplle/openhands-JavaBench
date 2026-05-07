/**
 * Lightweight string helper utilities.
 */
public final class StringUtils {
    private StringUtils() {
    }

    /**
     * Creates a string consisting of {@code ch} repeated {@code count} times.
     *
     * @param count repeat count
     * @param ch    target character
     * @return string of ch repeated count times
     */
    public static String createPadding(int count, char ch) {
        return String.valueOf(ch).repeat(Math.max(0, count));
    }
}
