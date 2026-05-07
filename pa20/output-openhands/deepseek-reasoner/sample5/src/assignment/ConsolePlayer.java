package assignment;

import java.util.Scanner;

public class ConsolePlayer extends Player {
    private static final Scanner scanner = new Scanner(System.in);

    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        String input = scanner.nextLine().trim();
        String[] parts = input.split("->");

        if (parts.length != 2) {
            System.out.println("Invalid input format. Use format: a1->b2");
            return nextMove(game, availableMoves);
        }

        try {
            Place source = parsePlace(parts[0]);
            Place destination = parsePlace(parts[1]);
            Move move = new Move(source, destination);

            // Validate the move against the rule pipeline
            Rule violated = game.validateMove(move);
            if (violated != null) {
                System.out.println(violated.getDescription());
                return nextMove(game, availableMoves);
            }

            return move;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid coordinate format.");
            return nextMove(game, availableMoves);
        }
    }

    private Place parsePlace(String s) {
        if (s.length() < 2) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
        char colChar = s.charAt(0);
        int row = Integer.parseInt(s.substring(1)) - 1;
        int col = colChar - 'a';
        return new Place(col, row);
    }
}
