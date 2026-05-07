import java.util.Objects;

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
        this.entity = null;
    }

    /**
     * Creates an entity cell at the specified position containing the given entity.
     *
     * @param position The position of this cell.
     * @param entity   The entity to place in this cell.
     */
    public EntityCell(final Position position, final Entity entity) {
        super(position);
        this.entity = Objects.requireNonNull(entity);
        this.entity.setOwner(this);
    }

    /**
     * @return The entity contained in this cell, or {@code null} if empty.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity contained in this cell, returning the previous entity.
     *
     * @param newEntity The new entity to place in this cell (may be {@code null}).
     * @return The previous entity in this cell, or {@code null} if the cell was empty.
     */
    public Entity setEntity(final Entity newEntity) {
        final Entity oldEntity = this.entity;
        if (this.entity != null) {
            this.entity.setOwner(null);
        }
        this.entity = newEntity;
        if (newEntity != null) {
            newEntity.setOwner(this);
        }
        return oldEntity;
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
