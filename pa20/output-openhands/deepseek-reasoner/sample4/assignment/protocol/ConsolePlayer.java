package assignment.protocol;

import java.util.Scanner;

/**
 * A human player that reads moves from the console.
 * Input format: "a1->b2" where columns are letters and rows are numbers.
 */
public class ConsolePlayer extends Player {
    private final transient Scanner scanner;

    public ConsolePlayer(String name, Color color) {
        super(name, color);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        System.out.println("Available moves for " + this.getColor() + this.name + Color.DEFAULT + ":");
        if (availableMoves.length == 0) {
            System.out.println("  (none)");
        } else {
            for (int i = 0; i < availableMoves.length; i++) {
                Move m = availableMoves[i];
                System.out.printf("  %d. %s%c%d%s->%s%c%d%s%n",
                        i + 1,
                        this.getColor(),
                        (char) ('a' + m.getSource().x()),
                        m.getSource().y() + 1,
                        Color.DEFAULT,
                        this.getColor(),
                        (char) ('a' + m.getDestination().x()),
                        m.getDestination().y() + 1,
                        Color.DEFAULT);
            }
        }

        while (true) {
            System.out.print("Enter your move (e.g., a1->b2): ");
            String input = scanner.nextLine().trim();

            Move move = parseMove(input, game.getConfiguration().getSize());
            if (move == null) {
                System.out.println("Invalid format. Please use format like a1->b2");
                continue;
            }

            // Check if move is in available moves
            boolean found = false;
            for (Move m : availableMoves) {
                if (m.equals(move)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                return move;
            }

            // Find which rule is violated and report it
            String error = findViolatedRule(game, move);
            if (error != null) {
                System.out.println("Invalid move: " + error);
            } else {
                System.out.println("Invalid move: not in available moves list");
            }
        }
    }

    /**
     * Parses a move string in the format "a1->b2".
     *
     * @param input the input string
     * @param size  the board size
     * @return the parsed Move, or null if the format is invalid
     */
    private Move parseMove(String input, int size) {
        String[] parts = input.split("->");
        if (parts.length != 2) {
            return null;
        }

        String src = parts[0].trim();
        String dst = parts[1].trim();

        if (src.length() < 2 || dst.length() < 2) {
            return null;
        }

        char srcCol = src.charAt(0);
        char dstCol = dst.charAt(0);

        if (srcCol < 'a' || srcCol >= 'a' + size || dstCol < 'a' || dstCol >= 'a' + size) {
            return null;
        }

        int srcX = srcCol - 'a';
        int dstX = dstCol - 'a';

        int srcY, dstY;
        try {
            srcY = Integer.parseInt(src.substring(1)) - 1;
            dstY = Integer.parseInt(dst.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        if (srcY < 0 || srcY >= size || dstY < 0 || dstY >= size) {
            return null;
        }

        return new Move(new Place(srcX, srcY), new Place(dstX, dstY));
    }

    /**
     * Finds the first rule violated by the given move.
     *
     * @param game the current game state
     * @param move the move to validate
     * @return the description of the first violated rule, or null if the move passes all rules
     */
    private String findViolatedRule(Game game, Move move) {
        int numProtected = game.getConfiguration().getNumMovesProtection();
        Rule[] rules = new Rule[]{
                new VacantRule(),
                new NilMoveRule(),
                new OutOfBoundaryRule(),
                new OccupiedRule(),
                new KnightMoveRule(),
                new KnightBlockRule(),
                new ArcherMoveRule(),
                new FirstNMovesProtectionRule(numProtected)
        };
        for (Rule rule : rules) {
            if (!rule.validate(game, move)) {
                return rule.getDescription();
            }
        }
        return null;
    }
}
