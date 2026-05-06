package game.map;

import game.Direction;

/**
 * Holder for termination cell creation information (coordinate and direction).
 */
public class TerminationCellCreateInfo {

    public final Coordinate coord;
    public final Direction dir;

    public TerminationCellCreateInfo(Coordinate coord, Direction dir) {
        this.coord = coord;
        this.dir = dir;
    }
}
