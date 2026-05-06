package game.map;

/**
 * Interface for map elements that can be rendered as a single character.
 */
public interface MapElement {

    /**
     * Returns the character representation of this element.
     *
     * @return A single character representation.
     */
    char toSingleChar();
}