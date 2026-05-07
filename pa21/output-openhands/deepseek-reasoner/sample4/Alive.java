import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a valid move result where the player survived the move.
 */
public class Alive extends Valid {

    private final List<Position> collectedGems;
    private final List<Position> collectedExtraLives;

    /**
     * Creates an alive move result.
     *
     * @param newPosition         The position after the move.
     * @param origPosition        The original position before the move.
     * @param collectedGems       The positions of gems collected during this move.
     * @param collectedExtraLives The positions of extra lives collected during this move.
     */
    public Alive(final Position newPosition, final Position origPosition,
                 final List<Position> collectedGems, final List<Position> collectedExtraLives) {
        super(newPosition, origPosition);
        this.collectedGems = Collections.unmodifiableList(Objects.requireNonNull(collectedGems));
        this.collectedExtraLives = Collections.unmodifiableList(Objects.requireNonNull(collectedExtraLives));
    }

    /**
     * Returns the positions of gems collected during this move.
     *
     * @return An unmodifiable list of collected gem positions.
     */
    public List<Position> getCollectedGems() {
        return collectedGems;
    }

    /**
     * Returns the positions of extra lives collected during this move.
     *
     * @return An unmodifiable list of collected extra life positions.
     */
    public List<Position> getCollectedExtraLives() {
        return collectedExtraLives;
    }
}
