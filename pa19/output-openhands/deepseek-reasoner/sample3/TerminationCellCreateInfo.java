/**
 * Holds coordinate and direction for creating a termination cell.
 */
public class TerminationCellCreateInfo {
    public final Coordinate coord;
    public final Direction dir;

    public TerminationCellCreateInfo(Coordinate coord, Direction dir) {
        this.coord = coord;
        this.dir = dir;
    }
}
