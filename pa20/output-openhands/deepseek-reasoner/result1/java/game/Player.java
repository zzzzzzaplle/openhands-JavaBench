package game;

import java.util.Objects;

/**
 * Abstract base class for players in the game.
 */
public abstract class Player {
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

    public Color getColor() {
        return color;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Get the next move for this player.
     * @param game the current game
     * @param availableMoves the list of available moves for this player
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
    public String toString() {
        return name + " (score: " + score + ")";
    }

    public Player clone() throws CloneNotSupportedException {
        Player cloned = (Player) super.clone();
        cloned.name = this.name;
        cloned.score = this.score;
        cloned.color = this.color;
        return cloned;
    }
}
