package assignment.game;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Reads player commands from a terminal input stream (e.g., System.in).
 * <p>
 * Supported key mappings:
 * <ul>
 *   <li>Player 0: W (up), A (left), S (down), D (right), R (undo)</li>
 *   <li>Player 1: K (up), H (left), J (down), L (right), U (undo)</li>
 *   <li>"exit"/"quit": Exit action with initiator -1</li>
 *   <li>Other input: InvalidInput action with initiator -1</li>
 * </ul>
 */
public class TerminalInputEngine implements InputEngine {

    private final Scanner terminalScanner;

    /**
     * Creates a TerminalInputEngine that reads from the given input stream.
     *
     * @param terminalStream the input stream (typically System.in)
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    @Override
    public Action fetchAction() {
        if (!terminalScanner.hasNextLine()) {
            return new Exit(-1);
        }
        String input = terminalScanner.nextLine().trim();

        if (input.equalsIgnoreCase(StringResources.EXIT_COMMAND_TEXT) || input.equalsIgnoreCase("quit")) {
            return new Exit(-1);
        }

        if (input.length() == 1) {
            char c = input.charAt(0);
            // Player 0 controls
            if (c == 'W' || c == 'w') return new Up(0);
            if (c == 'A' || c == 'a') return new Left(0);
            if (c == 'S' || c == 's') return new Down(0);
            if (c == 'D' || c == 'd') return new Right(0);
            if (c == 'R' || c == 'r') return new Undo(0);

            // Player 1 controls
            if (c == 'K' || c == 'k') return new Up(1);
            if (c == 'H' || c == 'h') return new Left(1);
            if (c == 'J' || c == 'j') return new Down(1);
            if (c == 'L' || c == 'l') return new Right(1);
            if (c == 'U' || c == 'u') return new Undo(1);
        }

        return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
    }
}
