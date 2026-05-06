import java.util.Objects;

/**
 * A special cell that stops the player's sliding movement.
 * Can only contain a Player entity.
 */
public class StopCell extends EntityCell {

    /**
     * Creates an empty stop cell at the given position.
     *
     * @param position The position of the cell.
     */
    public StopCell(final Position position) {
        super(Objects.requireNonNull(position));
    }

    /**
     * Creates a stop cell at the given position containing the specified entity.
     *
     * @param position   The position of the cell.
     * @param newEntity  The entity to place in this cell.
     */
    public StopCell(final Position position, final Entity newEntity) {
        super(Objects.requireNonNull(position), Objects.requireNonNull(newEntity));
    }

    @Override
    public Entity setEntity(final Entity newEntity) {
        // StopCell can only contain no entity (null) or a Player entity.
        // Reject setting a non-Player entity when already occupied or when
        // trying to place a non-Player entity.
        if (newEntity != null && !(newEntity instanceof Player)) {
            return getEntity();
        }
        return super.setEntity(newEntity);
    }

    /**
     * Convenience method to set a player in this stop cell.
     *
     * @param newPlayer The player to place.
     * @return The previous entity, or null if the cell was empty.
     */
    public Player setPlayer(final Player newPlayer) {
        return (Player) setEntity(newPlayer);
    }

    @Override
    public char toUnicodeChar() {
        return getEntity() != null ? getEntity().toUnicodeChar() : '\u25A1';
    }

    @Override
    public char toASCIIChar() {
        return getEntity() != null ? getEntity().toASCIIChar() : '#';
    }
}
