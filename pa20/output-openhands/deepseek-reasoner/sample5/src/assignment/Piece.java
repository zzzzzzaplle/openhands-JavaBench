package assignment;

public abstract class Piece {
    private final Player player;

    public Piece(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract char getLabel();

    public abstract Move[] getAvailableMoves(Game game, Place source);
}
