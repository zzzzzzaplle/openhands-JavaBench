package game;

/**
 * Mine entity that causes death.
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