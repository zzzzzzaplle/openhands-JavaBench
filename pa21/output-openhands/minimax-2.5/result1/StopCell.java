package game;

/**
 * A stop cell that can only contain a Player entity.
 */
public class StopCell extends EntityCell {
    public StopCell(Position position) {
        super(position);
    }

    public StopCell(Position position, Entity entity) {
        super(position, entity);
        // Only allow Player or null
        if (entity != null && !(entity instanceof Player)) {
            throw new IllegalArgumentException("StopCell can only contain a Player");
        }
    }

    @Override
    public void setEntity(Entity entity) {
        if (entity != null && !(entity instanceof Player)) {
            throw new IllegalArgumentException("StopCell can only contain a Player");
        }
        super.setEntity(entity);
    }

    /**
     * Set the player entity.
     */
    public Player setPlayer(Player newPlayer) {
        setEntity(newPlayer);
        return newPlayer;
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