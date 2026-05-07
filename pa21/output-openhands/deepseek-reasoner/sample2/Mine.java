/**
 * A mine entity that kills the player on contact.
 */
public class Mine extends Entity {

    public Mine() {
    }

    public Mine(final EntityCell owner) {
        super(owner);
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
