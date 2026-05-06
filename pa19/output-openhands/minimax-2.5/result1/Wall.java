package game.map;

/**
 * A wall cell in the map.
 */
public class Wall extends Cell {

    public Wall(Coordinate coord) {
        super(coord);
    }

    /**
     * Returns the wall character.
     *
     * @return The wall character from PipePatterns.
     */
    @Override
    public char toSingleChar() {
        return PipePatterns.WALL;
    }
}