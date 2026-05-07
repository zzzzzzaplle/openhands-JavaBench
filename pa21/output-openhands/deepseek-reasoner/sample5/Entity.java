import java.util.Objects;

/**
 * An abstract base class for all entities that can be placed on the game board.
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
     * Creates an entity owned by the specified cell.
     *
     * @param owner The cell that owns this entity.
     */
    protected Entity(final EntityCell owner) {
        this.owner = owner;
    }

    /**
     * @return The cell that owns this entity, or {@code null} if not placed on the board.
     */
    public EntityCell getOwner() {
        return owner;
    }

    /**
     * Sets the owner cell of this entity.
     *
     * @param owner The new owner cell.
     */
    public void setOwner(final EntityCell owner) {
        this.owner = owner;
    }
}
