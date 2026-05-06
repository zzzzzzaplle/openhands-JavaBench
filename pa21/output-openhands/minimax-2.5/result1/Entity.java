package game;

/**
 * Abstract base class for entities on the game board.
 */
public abstract class Entity implements BoardElement {
    private EntityCell owner;

    public EntityCell getOwner() {
        return owner;
    }

    public void setOwner(EntityCell owner) {
        this.owner = owner;
    }
}