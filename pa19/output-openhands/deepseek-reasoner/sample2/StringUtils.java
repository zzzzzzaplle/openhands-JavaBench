/**
 * Lightweight string helper utilities.
 */
public final class StringUtils {

    private StringUtils() {
        // non-instantiable
    }

    /**
     * Creates a string consisting of {@code ch} repeated {@code count} times.
     */
    public static String createPadding(int count, char ch) {
        return String.valueOf(ch).repeat(count);
    }
}
