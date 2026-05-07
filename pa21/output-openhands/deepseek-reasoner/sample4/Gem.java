/**
 * An entity that is collected for points.
 */
public class Gem extends Entity {

    @Override
    public char toUnicodeChar() {
        return '\u25C7';
    }

    @Override
    public char toASCIIChar() {
        return '*';
    }
}
