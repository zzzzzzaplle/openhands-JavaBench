/**
 * Interface for board elements that can be rendered as characters.
 */
public interface BoardElement {

    /**
     * Returns the Unicode character representation of this board element.
     *
     * @return A Unicode char representing this element.
     */
    char toUnicodeChar();

    /**
     * Returns the ASCII character representation of this board element.
     *
     * @return An ASCII char representing this element.
     */
    char toASCIIChar();
}
