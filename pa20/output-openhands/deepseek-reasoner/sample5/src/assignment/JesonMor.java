package assignment;

import java.util.ArrayList;
import java.util.List;

public class JesonMor extends Game {

    public JesonMor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Player start() {
        // Initialize board from configuration
        int size = configuration.getSize();
        this.board = new Piece[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                this.board[x][y] = configuration.getInitialBoard()[x][y];
            }
        }
        this.numMoves = 0;
        this.currentPlayer = configuration.getPlayers()[0];

        while (true) {
            refreshOutput();

            Move[] availableMoves = getAvailableMoves(currentPlayer);

            if (availableMoves.length == 0) {
                // No available moves: determine winner by score
                Player[] players = configuration.getPlayers();
                Player otherPlayer = players[0] == currentPlayer ? players[1] : players[0];
                if (currentPlayer.getScore() > otherPlayer.getScore()) {
                    return currentPlayer;
                } else if (otherPlayer.getScore() > currentPlayer.getScore()) {
                    return otherPlayer;
                } else {
                    return currentPlayer;
                }
            }

            Move chosenMove = currentPlayer.nextMove(this, availableMoves);
            if (chosenMove == null) {
                // Player couldn't make a move (e.g., RandomPlayer with empty list, though handled above)
                return currentPlayer;
            }

            Piece piece = getPiece(chosenMove.getSource());
            movePiece(chosenMove);
            updateScore(currentPlayer, piece, chosenMove);
            numMoves++;

            Player winner = getWinner(currentPlayer, piece, chosenMove);
            if (winner != null) {
                refreshOutput();
                return winner;
            }

            // Switch to the other player
            Player[] players = configuration.getPlayers();
            currentPlayer = currentPlayer == players[0] ? players[1] : players[0];
        }
    }

    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        // No winner during protection phase
        if (numMoves <= configuration.getNumMovesProtection()) {
            return null;
        }

        // Check if a Knight left the central square
        Place centralPlace = configuration.getCentralPlace();
        if (lastMove.getSource().equals(centralPlace)
                && !lastMove.getDestination().equals(centralPlace)
                && lastPiece instanceof Knight) {
            return lastPlayer;
        }

        // Check if only one player's pieces remain on the board
        Player[] players = configuration.getPlayers();
        boolean player0HasPieces = hasRemainingPieces(players[0]);
        boolean player1HasPieces = hasRemainingPieces(players[1]);

        if (player0HasPieces && !player1HasPieces) {
            return players[0];
        }
        if (!player0HasPieces && player1HasPieces) {
            return players[1];
        }

        return null;
    }

    private boolean hasRemainingPieces(Player player) {
        int size = configuration.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece p = board[x][y];
                if (p != null && p.getPlayer().equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateScore(Player player, Piece piece, Move move) {
        int dx = Math.abs(move.getDestination().x() - move.getSource().x());
        int dy = Math.abs(move.getDestination().y() - move.getSource().y());
        player.setScore(player.getScore() + dx + dy);
    }

    @Override
    public void movePiece(Move move) {
        Place src = move.getSource();
        Place dest = move.getDestination();
        board[dest.x()][dest.y()] = board[src.x()][src.y()];
        board[src.x()][src.y()] = null;
    }

    @Override
    public Move[] getAvailableMoves(Player player) {
        int size = configuration.getSize();
        List<Move> validMoves = new ArrayList<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                if (piece != null && piece.getPlayer().equals(player)) {
                    Place source = new Place(x, y);
                    Move[] candidates = piece.getAvailableMoves(this, source);
                    for (Move move : candidates) {
                        if (validateMove(move) == null) {
                            validMoves.add(move);
                        }
                    }
                }
            }
        }
        return validMoves.toArray(new Move[0]);
    }
}
