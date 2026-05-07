/**
 * An entity that kills the player on contact.
 */
public class Mine extends Entity {

    @Override
    public char toUnicodeChar() {
        return '\u26A0';
    }

    @Override
    public char toASCIIChar() {
        return 'X';
    }
}
