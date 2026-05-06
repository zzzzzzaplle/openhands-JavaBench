package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of a move attempt.
 */
public abstract class MoveResult {
    protected Position newPosition;

    public Position getNewPosition() {
        return newPosition;
    }

    /**
     * Result indicating the move was valid.
     */
    public static class Valid extends MoveResult {
        private Position origPosition;

        public Valid(Position origPosition, Position newPosition) {
            this.origPosition = origPosition;
            this.newPosition = newPosition;
        }

        public Position getOrigPosition() {
            return origPosition;
        }

        /**
         * Alive result - player is still alive.
         */
        public static class Alive extends Valid {
            private java.util.List<Position> collectedGems;
            private java.util.List<Position> collectedExtraLives;

            public Alive(Position origPosition, Position newPosition) {
                super(origPosition, newPosition);
                this.collectedGems = new java.util.ArrayList<>();
                this.collectedExtraLives = new java.util.ArrayList<>();
            }

            public void addCollectedGem(Position position) {
                collectedGems.add(position);
            }

            public void addCollectedExtraLife(Position position) {
                collectedExtraLives.add(position);
            }

            public java.util.List<Position> getCollectedGems() {
                return collectedGems;
            }

            public java.util.List<Position> getCollectedExtraLives() {
                return collectedExtraLives;
            }
        }

        /**
         * Dead result - player hit a mine.
         */
        public static class Dead extends Valid {
            public Dead(Position origPosition, Position newPosition) {
                super(origPosition, newPosition);
            }
        }
    }

    /**
     * Invalid result - player cannot move.
     */
    public static class Invalid extends MoveResult {
        public Invalid() {
            this.newPosition = null;
        }
    }
}