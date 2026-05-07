import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolePlayer extends Player {
    private static final Scanner scanner = new Scanner(System.in);

    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        Rule[] rules = getAllRules(game);

        while (true) {
            System.out.print(this.getColor() + this.getName() + Color.DEFAULT + "'s turn, input a move: ");
            String input = scanner.nextLine().trim();

            Move move = parseMove(input, game.getConfiguration().getSize());
            if (move == null) {
                System.out.println("Invalid input format. Use format like a1->b2.");
                continue;
            }

            // Validate the move against all rules
            String error = validateMove(game, move, rules);
            if (error != null) {
                System.out.println("Invalid move: " + error);
                continue;
            }

            return move;
        }
    }

    private Move parseMove(String input, int size) {
        if (input == null || !input.contains("->")) {
            return null;
        }

        String[] parts = input.split("->");
        if (parts.length != 2) {
            return null;
        }

        Place source = parsePlace(parts[0], size);
        Place dest = parsePlace(parts[1], size);

        if (source == null || dest == null) {
            return null;
        }

        return new Move(source, dest);
    }

    private Place parsePlace(String coord, int size) {
        if (coord == null || coord.length() < 2) {
            return null;
        }

        char colChar = coord.charAt(0);
        if (colChar < 'a' || colChar > 'a' + size - 1) {
            return null;
        }
        int x = colChar - 'a';

        int y;
        try {
            y = Integer.parseInt(coord.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        if (y < 0 || y >= size) {
            return null;
        }

        return new Place(x, y);
    }

    private String validateMove(Game game, Move move, Rule[] rules) {
        for (Rule rule : rules) {
            if (!rule.validate(game, move)) {
                return rule.getDescription();
            }
        }
        return null;
    }

    private Rule[] getAllRules(Game game) {
        int numMovesProtection = game.getConfiguration().getNumMovesProtection();
        return new Rule[]{
                new VacantRule(),
                new OutOfBoundaryRule(),
                new NilMoveRule(),
                new OccupiedRule(),
                new KnightMoveRule(),
                new KnightBlockRule(),
                new ArcherMoveRule(),
                new FirstNMovesProtectionRule(numMovesProtection)
        };
    }
}
