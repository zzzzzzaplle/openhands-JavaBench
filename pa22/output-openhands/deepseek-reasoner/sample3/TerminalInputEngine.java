import java.io.InputStream;
import java.util.Scanner;

/**
 * Reads player commands from a terminal InputStream.
 * <p>
 * Key bindings:
 * <ul>
 *   <li>Player 0: W(up), A(left), S(down), D(right), R(undo)</li>
 *   <li>Player 1: K(up), H(left), J(down), L(right), U(undo)</li>
 * </ul>
 * "exit" or "quit" produces an Exit action. Any other input produces InvalidInput.
 * </p>
 */
public class TerminalInputEngine implements InputEngine {

    private final Scanner terminalScanner;

    /**
     * Creates a TerminalInputEngine reading from the given input stream.
     *
     * @param terminalStream the input stream (e.g., System.in)
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    @Override
    public Action fetchAction() {
        if (!terminalScanner.hasNextLine()) {
            return new Exit(-1);
        }
        final String input = terminalScanner.nextLine().trim();

        if (input.equalsIgnoreCase(StringResources.EXIT_COMMAND_TEXT) || input.equalsIgnoreCase("quit")) {
            return new Exit(-1);
        }

        if (input.isEmpty()) {
            return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
        }

        final char key = Character.toUpperCase(input.charAt(0));

        switch (key) {
            // Player 0 controls
            case 'W':
                return new Up(0);
            case 'A':
                return new Left(0);
            case 'S':
                return new Down(0);
            case 'D':
                return new Right(0);
            case 'R':
                return new Undo(0);
            // Player 1 controls
            case 'K':
                return new Up(1);
            case 'H':
                return new Left(1);
            case 'J':
                return new Down(1);
            case 'L':
                return new Right(1);
            case 'U':
                return new Undo(1);
            default:
                return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
        }
    }
}
