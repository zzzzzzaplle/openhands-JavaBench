/**
 * Base interface for all map elements that can be rendered as a single character.
 */
public interface MapElement {
    /**
     * Returns the character representation of this map element.
     *
     * @return Single character for display
     */
    char toSingleChar();
}
