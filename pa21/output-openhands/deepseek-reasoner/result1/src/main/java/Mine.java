/**
 * An entity that kills the player on contact, causing death and life loss.
 */
public class Mine extends Entity {

    public Mine() {
        super();
    }

    @Override
    public char toUnicodeChar() {
        return '\u26A0';
    }

    @Override
    public char toASCIIChar() {
        return 'X';
    }
}
