package game;

/**
 * Extra life entity that increases player's lives.
 */
public class ExtraLife extends Entity {
    @Override
    public char toUnicodeChar() {
        return '\u2661';
    }

    @Override
    public char toASCIIChar() {
        return 'L';
    }
}