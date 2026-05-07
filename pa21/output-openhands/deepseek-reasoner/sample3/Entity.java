import java.util.Objects;

/**
 * Abstract base class for all entities that can be placed in an {@link EntityCell}.
 */
public abstract class Entity implements BoardElement {

    private EntityCell owner;

    /**
     * Creates an entity with no owning cell.
     */
    protected Entity() {
        this.owner = null;
    }

    /**
     * Returns the cell that owns this entity.
     *
     * @return The owning entity cell, or {@code null} if not placed.
     */
    public EntityCell getOwner() {
        return owner;
    }

    /**
     * Sets the owning cell for this entity.
     *
     * @param owner The new owning entity cell.
     */
    public void setOwner(final EntityCell owner) {
        this.owner = owner;
    }
}
