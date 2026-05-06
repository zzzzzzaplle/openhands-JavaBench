package game;

/**
 * Interface for board elements that can be displayed.
 */
public interface BoardElement {
    /**
     * Get Unicode character representation.
     */
    char toUnicodeChar();

    /**
     * Get ASCII character representation.
     */
    char toASCIIChar();
}