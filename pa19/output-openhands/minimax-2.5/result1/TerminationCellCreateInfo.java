package game.map;

/**
 * Helper class to hold termination cell creation information.
 */
public class TerminationCellCreateInfo {

    public final Coordinate coord;
    public final Direction dir;

    public TerminationCellCreateInfo(Coordinate coord, Direction dir) {
        this.coord = coord;
        this.dir = dir;
    }
}