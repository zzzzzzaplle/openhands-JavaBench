/**
 * A special cell that stops the player's sliding movement. Can only contain a Player entity.
 */
public class StopCell extends EntityCell {

    /**
     * Creates a stop cell at the given position with no entity.
     *
     * @param position The position of this cell.
     */
    public StopCell(final Position position) {
        super(position);
    }

    /**
     * Creates a stop cell at the given position containing the specified entity.
     *
     * @param position   The position of this cell.
     * @param newEntity The entity to place in this cell.
     */
    public StopCell(final Position position, final Entity newEntity) {
        super(position);
        setEntity(newEntity);
    }

    /**
     * Sets the player in this cell.
     *
     * @param newPlayer The player to set.
     * @return The previous player in this cell, or {@code null} if empty.
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
