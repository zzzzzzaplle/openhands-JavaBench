/**
 * A cell that can contain zero or one entity.
 */
public class EntityCell extends Cell {

    private Entity entity;

    /**
     * Creates an entity cell at the given position with no entity.
     *
     * @param position The position of this cell.
     */
    public EntityCell(final Position position) {
        super(position);
        this.entity = null;
    }

    /**
     * Creates an entity cell at the given position containing the specified entity.
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
     * @param entity The new entity, or {@code null} to clear.
     * @return The previous entity in this cell, or {@code null} if empty.
     */
    public Entity setEntity(final Entity entity) {
        final Entity old = this.entity;
        this.entity = entity;
        if (entity != null) {
            entity.setOwner(this);
        }
        return old;
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
