/**
 * Interface for all board elements (cells and entities) that can be rendered
 * as either Unicode or ASCII characters.
 */
public interface BoardElement {

    /**
     * @return The Unicode character representation of this board element.
     */
    char toUnicodeChar();

    /**
     * @return The ASCII character representation of this board element.
     */
    char toASCIIChar();
}
