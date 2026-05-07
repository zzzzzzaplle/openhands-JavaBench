package assignment.protocol;

import java.util.Objects;

/**
 * Abstract class representing a player in the game.
 */
public abstract class Player implements Cloneable {
    protected String name;
    protected int score;
    protected Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Prompts the player to choose a move from the available moves.
     *
     * @param game           the current game state
     * @param availableMoves array of valid moves available to the player
     * @return the chosen move
     */
    public abstract Move nextMove(Game game, Move[] availableMoves);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public Player clone() throws CloneNotSupportedException {
        return (Player) super.clone();
    }
}
