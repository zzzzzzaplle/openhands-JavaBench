import java.util.Objects;

/**
 * A cell that can contain an entity (player, gem, mine, extra life).
 */
public class EntityCell extends Cell {

    private Entity entity;

    /**
     * Creates an empty entity cell at the given position.
     *
     * @param position The position of the cell.
     */
    public EntityCell(final Position position) {
        super(Objects.requireNonNull(position));
        this.entity = null;
    }

    /**
     * Creates an entity cell at the given position containing the specified entity.
     *
     * @param position The position of the cell.
     * @param entity   The entity to place in this cell.
     */
    public EntityCell(final Position position, final Entity entity) {
        super(Objects.requireNonNull(position));
        this.entity = Objects.requireNonNull(entity);
        this.entity.setOwner(this);
    }

    /**
     * Returns the entity contained in this cell.
     *
     * @return The entity, or null if the cell is empty.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity contained in this cell and returns the previous entity.
     *
     * @param newEntity The new entity to place in this cell.
     * @return The previous entity, or null if the cell was empty.
     */
    public Entity setEntity(final Entity newEntity) {
        final Entity oldEntity = this.entity;
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
