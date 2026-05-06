package game;

import java.util.Scanner;

/**
 * A human player that chooses moves from the console.
 */
public class ConsolePlayer extends Player {
    private Scanner scanner;

    public ConsolePlayer(String name, Color color) {
        super(name, color);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        System.out.print("Available moves: ");
        for (int i = 0; i < availableMoves.length; i++) {
            System.out.print(availableMoves[i] + " ");
        }
        System.out.println();
        System.out.print(name + ", enter your move (e.g., a1->b2): ");

        String input = scanner.nextLine().trim();
        // Parse input in format "source->destination" like "a1->b2"
        String[] parts = input.split("->");
        if (parts.length != 2) {
            System.out.println("Invalid format. Use source->destination (e.g., a1->b2)");
            return nextMove(game, availableMoves);
        }

        try {
            Place source = parseCoordinate(parts[0].trim());
            Place destination = parseCoordinate(parts[1].trim());
            Move chosenMove = new Move(source, destination);

            // Check if the move is in available moves
            for (Move move : availableMoves) {
                if (move.equals(chosenMove)) {
                    return chosenMove;
                }
            }

            System.out.println("Invalid move. Try again.");
            return nextMove(game, availableMoves);
        } catch (Exception e) {
            System.out.println("Invalid coordinate format. Use letters and numbers (e.g., a1)");
            return nextMove(game, availableMoves);
        }
    }

    /**
     * Parse a coordinate string like "a1" to Place.
     * 'a' is column 0, 'b' is column 1, etc.
     * '1' is row 0, '2' is row 1, etc.
     */
    private Place parseCoordinate(String coord) throws IllegalArgumentException {
        coord = coord.trim().toLowerCase();
        if (coord.length() < 2) {
            throw new IllegalArgumentException("Coordinate too short");
        }

        char colChar = coord.charAt(0);
        String rowStr = coord.substring(1);

        int col = colChar - 'a';
        int row = Integer.parseInt(rowStr) - 1;

        return new Place(col, row);
    }
}
