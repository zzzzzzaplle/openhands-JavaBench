/**
 * Lightweight string helper utilities.
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Creates a string of the given length filled with the specified character.
     *
     * @param count repeat count
     * @param ch    target character
     * @return string made of {@code ch} repeated {@code count} times
     */
    public static String createPadding(int count, char ch) {
        return String.valueOf(ch).repeat(count);
    }
}
