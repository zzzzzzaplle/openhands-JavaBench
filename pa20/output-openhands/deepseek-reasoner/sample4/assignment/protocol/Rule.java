package assignment.protocol;

/**
 * Interface for validating moves in the game.
 * Each rule validates a specific constraint and provides a description when violated.
 */
public interface Rule {
    /**
     * Validates whether a move complies with this rule.
     *
     * @param game the current game state
     * @param move the move to validate
     * @return true if the move passes this rule, false otherwise
     */
    boolean validate(Game game, Move move);

    /**
     * Returns a human-readable description of this rule.
     *
     * @return rule description
     */
    String getDescription();
}
