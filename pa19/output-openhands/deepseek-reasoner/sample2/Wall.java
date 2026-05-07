/**
 * A cell representing an outer wall of the map.
 */
public class Wall extends Cell {

    public Wall(Coordinate coord) {
        super(coord);
    }

    @Override
    public char toSingleChar() {
        return PipePatterns.WALL;
    }
}
