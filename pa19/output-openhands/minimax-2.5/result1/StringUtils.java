package game.map;

/**
 * Lightweight string helper utilities.
 */
public final class StringUtils {

    private StringUtils() {
        // Prevent instantiation
    }

    /**
     * Creates a string consisting of a character repeated a specified number of times.
     *
     * @param count The number of times to repeat the character.
     * @param ch   The character to repeat.
     * @return A string made of ch repeated count times.
     */
    public static String createPadding(int count, char ch) {
        return String.valueOf(ch).repeat(count);
    }
}