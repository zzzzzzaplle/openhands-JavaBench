package game;

import game.pipe.Pipe;
import game.pipe.PipeShape;

import java.util.LinkedList;
import java.util.Random;

/**
 * Queue of upcoming pipes with auto-refill.
 */
public class PipeQueue {

    private static final int MAX_GEN_LENGTH = 5;
    private static final Random RANDOM = new Random();
    private static final PipeShape[] SHAPES = PipeShape.values();

    private final LinkedList<Pipe> pipeQueue;

    /**
     * Constructs a PipeQueue with the given initial pipes.
     *
     * @param initialPipes initial list of pipes; if fewer than MAX_GEN_LENGTH, fills remaining with random pipes
     */
    public PipeQueue(java.util.List<Pipe> initialPipes) {
        this.pipeQueue = new LinkedList<>();

        if (initialPipes != null) {
            pipeQueue.addAll(initialPipes);
        }
        while (pipeQueue.size() < MAX_GEN_LENGTH) {
            pipeQueue.add(generateNewPipe());
        }
    }

    /**
     * Peeks at the head pipe without removing it.
     *
     * @return head pipe
     */
    public Pipe peek() {
        return pipeQueue.peekFirst();
    }

    /**
     * Consumes (removes) the head pipe and refills the queue.
     */
    public void consume() {
        pipeQueue.pollFirst();
        pipeQueue.addLast(generateNewPipe());
    }

    /**
     * Inserts a pipe back at the front of the queue.
     *
     * @param pipe pipe to undo
     */
    public void undo(Pipe pipe) {
        pipeQueue.addFirst(pipe);
    }

    /**
     * Generates a random pipe.
     *
     * @return random pipe
     */
    private static Pipe generateNewPipe() {
        PipeShape shape = SHAPES[RANDOM.nextInt(SHAPES.length)];
        return new Pipe(shape);
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
