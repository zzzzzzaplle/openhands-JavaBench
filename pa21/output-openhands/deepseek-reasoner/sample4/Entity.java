/**
 * Abstract base class for all entities that can be placed in an {@link EntityCell}.
 */
public abstract class Entity implements BoardElement {

    private EntityCell owner;

    /**
     * Creates an entity with no owner.
     */
    public Entity() {
    }

    /**
     * Returns the owner cell of this entity.
     *
     * @return The owner {@link EntityCell}, or {@code null} if not placed on any cell.
     */
    public EntityCell getOwner() {
        return owner;
    }

    /**
     * Sets the owner cell of this entity.
     *
     * @param owner The owner {@link EntityCell} to set.
     */
    public void setOwner(final EntityCell owner) {
        this.owner = owner;
    }
}
