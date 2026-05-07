/**
 * A cell that can contain an {@link Entity}.
 */
public class EntityCell extends Cell {

    private Entity entity;

    /**
     * Creates an empty entity cell at the specified position.
     *
     * @param position The position of this cell.
     */
    public EntityCell(final Position position) {
        super(position);
    }

    /**
     * Creates an entity cell at the specified position containing the given entity.
     *
     * @param position The position of this cell.
     * @param entity   The entity to place in this cell.
     */
    public EntityCell(final Position position, final Entity entity) {
        super(position);
        this.entity = entity;
        if (entity != null) {
            entity.setOwner(this);
        }
    }

    /**
     * Returns the entity contained in this cell.
     *
     * @return The entity, or {@code null} if empty.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity contained in this cell.
     *
     * @param entity The entity to place, or {@code null} to clear.
     */
    public void setEntity(final Entity entity) {
        this.entity = entity;
        if (entity != null) {
            entity.setOwner(this);
        }
    }

    @Override
    public char toUnicodeChar() {
        return entity != null ? entity.toUnicodeChar() : '.';
    }

    @Override
    public char toASCIIChar() {
        return entity != null ? entity.toASCIIChar() : '.';
    }
}
