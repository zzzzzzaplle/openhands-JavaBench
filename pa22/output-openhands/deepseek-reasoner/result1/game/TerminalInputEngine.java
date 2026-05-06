package assignment.game;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Terminal-based input engine that reads commands from a Scanner.
 * <p>
 * Player 0: W (up), A (left), S (down), D (right), R (undo)
 * Player 1: K (up), H (left), J (down), L (right), U (undo)
 */
public class TerminalInputEngine implements InputEngine {
    private final Scanner terminalScanner;

    /**
     * Create a terminal input engine reading from the given input stream.
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
        String input = terminalScanner.nextLine().trim().toLowerCase();

        switch (input) {
            // Player 0 controls
            case "w":
                return new Up(0);
            case "a":
                return new Left(0);
            case "s":
                return new Down(0);
            case "d":
                return new Right(0);
            case "r":
                return new Undo(0);

            // Player 1 controls
            case "k":
                return new Up(1);
            case "h":
                return new Left(1);
            case "j":
                return new Down(1);
            case "l":
                return new Right(1);
            case "u":
                return new Undo(1);

            // Exit commands
            case "exit":
            case "quit":
                return new Exit(-1);

            default:
                return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
        }
    }
}
