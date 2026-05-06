/**
 * Interface for elements on the game board that have a textual representation.
 */
public interface BoardElement {

    /**
     * Returns the Unicode character representation of this board element.
     *
     * @return Unicode character.
     */
    char toUnicodeChar();

    /**
     * Returns the ASCII character representation of this board element.
     *
     * @return ASCII character.
     */
    char toASCIIChar();
}
