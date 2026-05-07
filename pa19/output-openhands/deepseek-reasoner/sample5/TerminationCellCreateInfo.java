/**
 * Simple holder for termination cell creation parameters.
 */
public class TerminationCellCreateInfo {
    public final Coordinate coord;
    public final Direction dir;

    public TerminationCellCreateInfo(Coordinate coord, Direction dir) {
        this.coord = coord;
        this.dir = dir;
    }
}
