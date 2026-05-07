import java.util.Objects;

/**
 * A special cell that stops the player's sliding movement and can only contain a {@link Player}.
 */
public class StopCell extends EntityCell {

    /**
     * Creates an empty stop cell at the specified position.
     *
     * @param position The position of this stop cell.
     */
    public StopCell(final Position position) {
        super(position);
    }

    /**
     * Creates a stop cell at the specified position containing the given player.
     *
     * @param position The position of this stop cell.
     * @param player   The player to place.
     */
    public StopCell(final Position position, final Player player) {
        super(position, player);
    }

    /**
     * Sets the entity in this cell. Only {@link Player} instances are allowed.
     *
     * @param entity The entity to set.
     * @throws IllegalArgumentException if entity is not a Player.
     */
    @Override
    public void setEntity(final Entity entity) {
        if (entity != null && !(entity instanceof Player)) {
            throw new IllegalArgumentException("StopCell can only contain a Player entity");
        }
        super.setEntity(entity);
    }

    /**
     * Places a player in this cell.
     *
     * @param player The player to place.
     * @return The player that was placed.
     */
    public Player setPlayer(final Player player) {
        Objects.requireNonNull(player);
        super.setEntity(player);
        return player;
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
