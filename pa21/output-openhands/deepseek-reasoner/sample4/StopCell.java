/**
 * A special cell that stops player movement. Can only contain a Player entity.
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
     * @param player   The player to place in this cell.
     */
    public StopCell(final Position position, final Player player) {
        super(position, player);
    }

    /**
     * Sets the entity in this cell. Only {@link Player} instances are allowed.
     *
     * @param newEntity The entity to place. Must be a {@link Player} or {@code null}.
     * @return The previous entity.
     * @throws IllegalArgumentException if {@code newEntity} is not a {@link Player} and not {@code null}.
     */
    @Override
    public Entity setEntity(final Entity newEntity) {
        if (newEntity != null && !(newEntity instanceof Player)) {
            throw new IllegalArgumentException("StopCell can only contain a Player entity");
        }
        return super.setEntity(newEntity);
    }

    /**
     * Convenience method to set a player in this cell.
     *
     * @param newPlayer The player to set.
     * @return The previous entity.
     */
    public Player setPlayer(final Player newPlayer) {
        return (Player) setEntity((Entity) newPlayer);
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
