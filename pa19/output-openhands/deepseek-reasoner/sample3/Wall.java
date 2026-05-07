/**
 * A wall cell that cannot contain pipes.
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
