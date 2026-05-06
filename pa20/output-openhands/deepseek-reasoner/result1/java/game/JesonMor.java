package game;

import rule.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The JesonMor game implementation.
 * A turn-based, two-player board game with Knight and Archer pieces.
 */
public class JesonMor extends Game {
    private List<Rule> allRules;

    /**
     * Create a new JesonMor game with the given configuration.
     */
    public JesonMor(Configuration configuration) {
        super();
        initialize(configuration);

        // Initialize all validation rules
        this.allRules = new ArrayList<>();
        int numProtectedMoves = configuration.getNumMovesProtection();

        // Global rules
        allRules.add(new VacantRule());
        allRules.add(new NilMoveRule());
        allRules.add(new OutOfBoundaryRule());
        allRules.add(new OccupiedRule());

        // Protection rule (only if protection is enabled)
        if (numProtectedMoves > 0) {
            allRules.add(new FirstNMovesProtectionRule(numProtectedMoves));
        }

        // Piece-specific rules
        allRules.add(new KnightMoveRule());
        allRules.add(new KnightBlockRule());
        allRules.add(new ArcherMoveRule());

        // Reference rules list from Game for isValidMove
        this.rules = allRules;
    }

    @Override
    public Player start() {
        Player winner = null;

        while (winner == null) {
            // Display the board
            refreshOutput();

            // Get available moves for current player
            Move[] availableMoves = getAvailableMoves(currentPlayer);

            // Check if player has no available moves
            if (availableMoves == null || availableMoves.length == 0) {
                // Game ends, winner is decided by score comparison
                Player[] players = configuration.getPlayers();
                if (players[0].getScore() > players[1].getScore()) {
                    winner = players[0];
                } else if (players[1].getScore() > players[0].getScore()) {
                    winner = players[1];
                } else {
                    // Tie goes to current player
                    winner = currentPlayer;
                }
                break;
            }

            // Get next move from current player
            Move move = currentPlayer.nextMove(this, availableMoves);

            // Validate move using rule pipeline
            Rule violatedRule = getViolatedRule(move);
            if (violatedRule != null) {
                System.out.println("Invalid move: " + violatedRule.getDescription());
                continue;
            }

            // Execute the move
            Piece piece = getPiece(move.getSource());
            movePiece(move);

            // Update score
            updateScore(currentPlayer, piece, move);

            // Increment move counter
            numMoves++;

            // Check for winner (if we're past protection phase)
            if (numMoves >= configuration.getNumMovesProtection()) {
                winner = getWinner(currentPlayer, piece, move);
            }

            // Switch to other player
            Player[] players = configuration.getPlayers();
            if (currentPlayer == players[0]) {
                currentPlayer = players[1];
            } else {
                currentPlayer = players[0];
            }
        }

        // Display final board
        refreshOutput();

        System.out.println("Game over! Winner: " + winner.getName());
        return winner;
    }

    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        Place centralPlace = configuration.getCentralPlace();

        // Win condition 1: Knight leaves the central square
        if (lastPiece instanceof Knight) {
            Place source = lastMove.getSource();
            Place dest = lastMove.getDestination();

            if (source.equals(centralPlace) && !dest.equals(centralPlace)) {
                return lastPlayer;
            }
        }

        // Win condition 2: Only one player's pieces remain
        Player[] players = configuration.getPlayers();
        int countPlayer0 = countPieces(players[0]);
        int countPlayer1 = countPieces(players[1]);

        if (countPlayer0 == 0) {
            return players[1];
        } else if (countPlayer1 == 0) {
            return players[0];
        }

        // No winner yet
        return null;
    }

    /**
     * Count pieces belonging to a player.
     */
    private int countPieces(Player player) {
        int count = 0;
        int size = configuration.getSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                if (piece != null && piece.getPlayer() == player) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public void updateScore(Player player, Piece piece, Move move) {
        // Score = Manhattan distance between source and destination
        int sx = move.getSource().x();
        int sy = move.getSource().y();
        int dx = move.getDestination().x();
        int dy = move.getDestination().y();

        int manhattanDistance = Math.abs(dx - sx) + Math.abs(dy - sy);
        player.addScore(manhattanDistance);
    }

    @Override
    public void movePiece(Move move) {
        // Get the piece at source
        Piece piece = getPiece(move.getSource());

        // Move it to destination
        setPiece(move.getDestination(), piece);

        // Clear the source
        setPiece(move.getSource(), null);
    }

    @Override
    public Move[] getAvailableMoves(Player player) {
        return getAllMovesForPlayer(player);
    }
}
