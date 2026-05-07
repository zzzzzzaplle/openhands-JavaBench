import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A valid move result where the player survived the move.
 */
public class Alive extends Valid {

    private final List<Position> collectedGems;
    private final List<Position> collectedExtraLives;

    /**
     * Creates an alive move result.
     *
     * @param origPosition       The original position of the player.
     * @param newPosition        The position the player ended up at.
     * @param collectedGems      The positions of gems collected during this move.
     * @param collectedExtraLives The positions of extra lives collected during this move.
     */
    public Alive(final Position origPosition, final Position newPosition,
                 final List<Position> collectedGems, final List<Position> collectedExtraLives) {
        super(origPosition, newPosition);
        this.collectedGems = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(collectedGems)));
        this.collectedExtraLives = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(collectedExtraLives)));
    }

    /**
     * Returns the positions of gems collected during this move.
     *
     * @return An unmodifiable list of gem positions.
     */
    public List<Position> getCollectedGems() {
        return collectedGems;
    }

    /**
     * Returns the positions of extra lives collected during this move.
     *
     * @return An unmodifiable list of extra life positions.
     */
    public List<Position> getCollectedExtraLives() {
        return collectedExtraLives;
    }
}
