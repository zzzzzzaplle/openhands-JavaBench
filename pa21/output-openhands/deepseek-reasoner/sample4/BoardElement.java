/**
 * Interface for all elements that can appear on the game board.
 */
public interface BoardElement {

    /**
     * Returns the Unicode character representation of this board element.
     *
     * @return A Unicode character.
     */
    char toUnicodeChar();

    /**
     * Returns the ASCII character representation of this board element.
     *
     * @return An ASCII character.
     */
    char toASCIIChar();
}
