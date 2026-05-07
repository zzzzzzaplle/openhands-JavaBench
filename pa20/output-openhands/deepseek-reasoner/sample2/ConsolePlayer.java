import java.util.Scanner;

public class ConsolePlayer extends Player {
    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        if (availableMoves == null || availableMoves.length == 0) {
            return null;
        }

        Scanner scanner = new Scanner(System.in);
        Rule[] validationRules = getValidationRules(game);

        while (true) {
            System.out.print(this.color + this.name + Color.DEFAULT + "'s turn, please input your move: ");
            String input = scanner.nextLine().trim();

            Move move = parseMove(input, game.getConfiguration().getSize());
            if (move == null) {
                System.out.println("Invalid input format. Use format like a1->b2");
                continue;
            }

            // Check if the move is in available moves
            boolean found = false;
            for (Move avail : availableMoves) {
                if (avail.equals(move)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                // Check which rule is violated
                String violation = getFirstViolation(game, move, validationRules);
                if (violation != null) {
                    System.out.println("The move is invalid: " + violation);
                } else {
                    System.out.println("The move is invalid: unknown reason");
                }
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

        Place src = parsePlace(parts[0], size);
        if (src == null) {
            return null;
        }
        Place dst = parsePlace(parts[1], size);
        if (dst == null) {
            return null;
        }

        return new Move(src, dst);
    }

    private Place parsePlace(String s, int size) {
        if (s == null || s.length() < 2) {
            return null;
        }

        char colChar = s.charAt(0);
        if (colChar < 'a' || colChar > 'a' + size - 1) {
            return null;
        }
        int x = colChar - 'a';

        String rowStr = s.substring(1);
        int y;
        try {
            y = Integer.parseInt(rowStr) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        if (y < 0 || y >= size) {
            return null;
        }

        return new Place(x, y);
    }

    private Rule[] getValidationRules(Game game) {
        return new Rule[]{
                new OutOfBoundaryRule(),
                new VacantRule(),
                new NilMoveRule(),
                new OccupiedRule(),
                new FirstNMovesProtectionRule(game.getConfiguration().getNumMovesProtection()),
                new KnightMoveRule(),
                new KnightBlockRule(),
                new ArcherMoveRule()
        };
    }

    private String getFirstViolation(Game game, Move move, Rule[] rules) {
        for (Rule rule : rules) {
            if (!rule.validate(game, move)) {
                return rule.getDescription();
            }
        }
        return null;
    }
}
