import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A valid move where the player survived (no mine hit).
 */
public class Alive extends Valid {

    private final List<Position> collectedGems;
    private final List<Position> collectedExtraLives;

    /**
     * Creates an alive move result.
     *
     * @param origPosition       The position before the move.
     * @param newPosition        The position after the move.
     * @param collectedGems      The positions of gems collected during this move.
     * @param collectedExtraLives The positions of extra lives collected during this move.
     */
    public Alive(final Position origPosition, final Position newPosition,
                 final List<Position> collectedGems,
                 final List<Position> collectedExtraLives) {
        super(Objects.requireNonNull(origPosition), Objects.requireNonNull(newPosition));
        this.collectedGems = Collections.unmodifiableList(
                Objects.requireNonNull(collectedGems));
        this.collectedExtraLives = Collections.unmodifiableList(
                Objects.requireNonNull(collectedExtraLives));
    }

    /**
     * Returns the positions of gems collected during this move.
     *
     * @return List of collected gem positions.
     */
    public List<Position> getCollectedGems() {
        return collectedGems;
    }

    /**
     * Returns the positions of extra lives collected during this move.
     *
     * @return List of collected extra life positions.
     */
    public List<Position> getCollectedExtraLives() {
        return collectedExtraLives;
    }
}
