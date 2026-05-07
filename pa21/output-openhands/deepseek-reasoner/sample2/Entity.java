import java.util.Objects;

/**
 * Abstract base class for all entities that can occupy an {@link EntityCell}.
 */
public abstract class Entity implements BoardElement {

    private EntityCell owner;

    /**
     * Creates an entity with no owner.
     */
    protected Entity() {
    }

    /**
     * Creates an entity owned by the specified cell.
     *
     * @param owner The cell that owns this entity.
     */
    protected Entity(final EntityCell owner) {
        this.owner = Objects.requireNonNull(owner);
    }

    /**
     * Returns the cell that owns this entity.
     *
     * @return The owning cell, or {@code null} if not owned.
     */
    public EntityCell getOwner() {
        return owner;
    }

    /**
     * Sets the cell that owns this entity.
     *
     * @param owner The new owning cell.
     */
    public void setOwner(final EntityCell owner) {
        this.owner = owner;
    }
}
