package rule;

import game.Game;
import game.Move;

/**
 * Interface for move validation rules.
 */
public interface Rule {
    /**
     * Validate a move against this rule.
     * @param game the current game
     * @param move the move to validate
     * @return true if the move is valid, false otherwise
     */
    boolean validate(Game game, Move move);

    /**
     * Get a description of this rule.
     * @return description string
     */
    String getDescription();
}