import java.util.Objects;

/**
 * Abstract base class representing the result of a move.
 */
public abstract class MoveResult {

    private final Position newPosition;

    /**
     * Creates a move result with the specified new position.
     *
     * @param newPosition The position the player ended up at.
     */
    protected MoveResult(final Position newPosition) {
        this.newPosition = Objects.requireNonNull(newPosition);
    }

    /**
     * @return The position the player ended up at after this move.
     */
    public Position getNewPosition() {
        return newPosition;
    }

    /**
     * A valid move that actually occurred (Alive or Dead).
     */
    public abstract static class Valid extends MoveResult {

        private final Position origPosition;

        /**
         * Creates a valid move result.
         *
         * @param newPosition  The position the player ended up at.
         * @param origPosition The position the player started from.
         */
        protected Valid(final Position newPosition, final Position origPosition) {
            super(newPosition);
            this.origPosition = Objects.requireNonNull(origPosition);
        }

        /**
         * @return The position the player started from.
         */
        public Position getOrigPosition() {
            return origPosition;
        }

        /**
         * A move where the player survived (collected items, reached destination).
         */
        public static class Alive extends Valid {

            private final java.util.List<Position> collectedGems;
            private final java.util.List<Position> collectedExtraLives;

            /**
             * Creates an alive move result.
             *
             * @param newPosition         The position the player ended up at.
             * @param origPosition        The position the player started from.
             * @param collectedGems       The positions of gems collected during this move.
             * @param collectedExtraLives The positions of extra lives collected during this move.
             */
            public Alive(final Position newPosition, final Position origPosition,
                         final java.util.List<Position> collectedGems,
                         final java.util.List<Position> collectedExtraLives) {
                super(newPosition, origPosition);
                this.collectedGems = new java.util.ArrayList<>(
                        Objects.requireNonNull(collectedGems));
                this.collectedExtraLives = new java.util.ArrayList<>(
                        Objects.requireNonNull(collectedExtraLives));
            }

            /**
             * @return The positions of gems collected during this move.
             */
            public java.util.List<Position> getCollectedGems() {
                return collectedGems;
            }

            /**
             * @return The positions of extra lives collected during this move.
             */
            public java.util.List<Position> getCollectedExtraLives() {
                return collectedExtraLives;
            }
        }

        /**
         * A move where the player hit a mine and died.
         */
        public static class Dead extends Valid {

            /**
             * Creates a dead move result. The player's position is reverted to the original position.
             *
             * @param origPosition The position the player started from (and reverts to).
             */
            public Dead(final Position origPosition) {
                super(origPosition, origPosition);
            }
        }
    }

    /**
     * An invalid move where the player could not move at all.
     */
    public static class Invalid extends MoveResult {

        /**
         * Creates an invalid move result.
         *
         * @param position The player's current position (unchanged).
         */
        public Invalid(final Position position) {
            super(position);
        }
    }
}
