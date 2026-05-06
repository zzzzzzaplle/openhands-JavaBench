import java.util.Objects;

/**
 * A wall cell that blocks movement. The player cannot move through or into a wall.
 */
public class Wall extends Cell {

    /**
     * Creates a wall at the given position.
     *
     * @param position The position of the wall on the game board.
     */
    public Wall(final Position position) {
        super(Objects.requireNonNull(position));
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
