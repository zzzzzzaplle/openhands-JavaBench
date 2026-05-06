package game.map;

/**
 * Interface for all elements that can be rendered on the map.
 */
public interface MapElement {

    /**
     * Returns the single character representation of this map element.
     *
     * @return rendering character
     */
    char toSingleChar();
}
