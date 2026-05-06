package game.map;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Queue of pipes for player to place.
 */
public class PipeQueue {

    private static final int MAX_GEN_LENGTH = 5;
    private final LinkedList<Pipe> pipeQueue;
    private static final Random random = new Random();

    public PipeQueue() {
        this.pipeQueue = new LinkedList<>();
        refill();
    }

    /**
     * Constructs a pipe queue with a given list of initial pipes.
     *
     * @param pipes Initial pipes.
     */
    public PipeQueue(List<Pipe> pipes) {
        this.pipeQueue = new LinkedList<>(pipes);
        refill();
    }

    /**
     * Peeks at the head of the queue without removing.
     *
     * @return The pipe at the head, or null if empty.
     */
    public Pipe peek() {
        return pipeQueue.isEmpty() ? null : pipeQueue.getFirst();
    }

    /**
     * Consumes the head pipe and removes it from the queue.
     */
    public void consume() {
        if (!pipeQueue.isEmpty()) {
            pipeQueue.removeFirst();
            refill();
        }
    }

    /**
     * Inserts a pipe back to the front of the queue.
     *
     * @param pipe The pipe to undo.
     */
    public void undo(Pipe pipe) {
        pipeQueue.addFirst(pipe);
    }

    /**
     * Refills the queue to maintain MAX_GEN_LENGTH.
     */
    private void refill() {
        while (pipeQueue.size() < MAX_GEN_LENGTH) {
            pipeQueue.addLast(generateNewPipe());
        }
    }

    /**
     * Generates a new random pipe.
     *
     * @return A new pipe with random shape.
     */
    private static Pipe generateNewPipe() {
        PipeShape[] shapes = PipeShape.values();
        return new Pipe(shapes[random.nextInt(shapes.length)]);
    }

    /**
     * Displays the pipe queue.
     */
    public void display() {
        System.out.print("Next Pipes:  ");
        for (Pipe p : pipeQueue) {
            System.out.print(p.toSingleChar() + "    ");
        }
        System.out.println();
    }
}