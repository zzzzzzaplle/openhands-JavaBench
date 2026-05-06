package game;

import java.util.Objects;

/**
 * Represents a move in the game, with source and destination places.
 */
public class Move {
    private Place source;
    private Place destination;

    public Move(Place source, Place destination) {
        this.source = source;
        this.destination = destination;
    }

    public Place getSource() {
        return source;
    }

    public Place getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return source.equals(move.source) &&
                destination.equals(move.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }

    @Override
    public String toString() {
        return source.toString() + "->" + destination.toString();
    }

    public Move clone() throws CloneNotSupportedException {
        return new Move(this.source.clone(), this.destination.clone());
    }
}