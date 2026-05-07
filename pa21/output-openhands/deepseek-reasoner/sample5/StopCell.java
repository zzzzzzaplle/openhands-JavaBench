/**
 * A special cell that can only contain a {@link Player} entity. Sliding stops exactly on this cell.
 */
public class StopCell extends EntityCell {

    /**
     * Creates an empty stop cell at the specified position.
     *
     * @param position The position of this cell.
     */
    public StopCell(final Position position) {
        super(position);
    }

    /**
     * Creates a stop cell at the specified position containing the given entity.
     *
     * @param position The position of this cell.
     * @param entity   The entity to place in this cell.
     */
    public StopCell(final Position position, final Entity entity) {
        super(position, entity);
    }

    @Override
    public Entity setEntity(final Entity newEntity) {
        if (newEntity != null && !(newEntity instanceof Player)) {
            throw new IllegalArgumentException("StopCell can only contain a Player entity");
        }
        return super.setEntity(newEntity);
    }

    /**
     * Convenience method to set the player in this stop cell.
     *
     * @param newPlayer The new player to place in this cell (may be {@code null}).
     * @return The previous player in this cell, or {@code null} if the cell was empty.
     */
    public Player setPlayer(final Player newPlayer) {
        final Entity old = setEntity(newPlayer);
        return (Player) old;
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
