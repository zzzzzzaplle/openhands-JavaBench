/**
 * A cell that can contain an entity.
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
     * Sets the entity in this cell.
     *
     * @param newEntity The entity to place, or {@code null} to clear.
     * @return The previous entity, or {@code null} if none.
     */
    public Entity setEntity(final Entity newEntity) {
        final Entity oldEntity = this.entity;
        this.entity = newEntity;
        if (newEntity != null) {
            newEntity.setOwner(this);
        }
        if (oldEntity != null) {
            oldEntity.setOwner(null);
        }
        return oldEntity;
    }

    /**
     * Removes and returns the entity from this cell.
     *
     * @return The removed entity, or {@code null} if none.
     */
    public Entity removeEntity() {
        return setEntity(null);
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
