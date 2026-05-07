/**
 * A wall cell that blocks player movement.
 */
public class Wall extends Cell {

    /**
     * Creates a wall at the specified position.
     *
     * @param position The position of this wall.
     */
    public Wall(final Position position) {
        super(position);
    }

    @Override
    public char toUnicodeChar() {
        return '\u2588';
    }

    @Override
    public char toASCIIChar() {
        return 'W';
    }
}
