/**
 * An entity representing a gem that can be collected by the player.
 */
public class Gem extends Entity {

    public Gem() {
    }

    public Gem(final EntityCell owner) {
        super(owner);
    }

    @Override
    public char toUnicodeChar() {
        return '\u25C7';
    }

    @Override
    public char toASCIIChar() {
        return '*';
    }
}
