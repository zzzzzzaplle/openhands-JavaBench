import java.util.Objects;

/**
 * Abstract base class for all entities that can be placed in an {@link EntityCell}.
 */
public abstract class Entity implements BoardElement {

    private EntityCell owner;

    /**
     * Creates an entity with no owner.
     */
    protected Entity() {
        this.owner = null;
    }

    /**
     * Creates an entity owned by the given cell.
     *
     * @param owner The owning entity cell.
     */
    protected Entity(final EntityCell owner) {
        this.owner = Objects.requireNonNull(owner);
    }

    /**
     * Returns the entity cell that owns this entity.
     *
     * @return The owning entity cell, or null if not set.
     */
    public EntityCell getOwner() {
        return owner;
    }

    /**
     * Sets the owning entity cell for this entity.
     *
     * @param owner The owning entity cell.
     */
    public void setOwner(final EntityCell owner) {
        this.owner = owner;
    }
}
