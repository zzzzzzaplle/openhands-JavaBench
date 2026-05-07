import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * A queue of pipes that the player can place. Auto-refills to a fixed length
 * with randomly generated pipes.
 */
public class PipeQueue implements Iterable<Pipe> {

    private static final int MAX_GEN_LENGTH = 5;
    private static final Random RANDOM = new Random();

    private final LinkedList<Pipe> pipeQueue;

    public PipeQueue() {
        this.pipeQueue = new LinkedList<>();
        fillQueue();
    }

    /**
     * Creates a PipeQueue initialized with the given pipes.
     *
     * @param pipes Initial list of pipes
     */
    public PipeQueue(java.util.List<Pipe> pipes) {
        this.pipeQueue = new LinkedList<>(pipes);
        fillQueue();
    }

    /**
     * Peeks at the head pipe without removing it.
     *
     * @return The pipe at the front of the queue
     */
    public Pipe peek() {
        return pipeQueue.peekFirst();
    }

    /**
     * Consumes (removes) the head pipe and refills the queue.
     */
    public void consume() {
        pipeQueue.pollFirst();
        fillQueue();
    }

    /**
     * Inserts a pipe back to the front of the queue (for undo).
     *
     * @param pipe Pipe to restore
     */
    public void undo(Pipe pipe) {
        pipeQueue.addFirst(pipe);
    }

    /**
     * Displays the next pipes in the queue.
     */
    public void display() {
        System.out.print("Next Pipes:  ");
        for (Pipe p : pipeQueue) {
            System.out.print(p.toSingleChar() + "    ");
        }
        System.out.println();
    }

    /**
     * Generates a random pipe shape.
     *
     * @return A new Pipe with random shape
     */
    private static Pipe generateNewPipe() {
        PipeShape[] shapes = PipeShape.values();
        return new Pipe(shapes[RANDOM.nextInt(shapes.length)]);
    }

    /**
     * Fills the queue up to MAX_GEN_LENGTH with random pipes.
     */
    private void fillQueue() {
        while (pipeQueue.size() < MAX_GEN_LENGTH) {
            pipeQueue.addLast(generateNewPipe());
        }
    }

    @Override
    public Iterator<Pipe> iterator() {
        return pipeQueue.iterator();
    }
}
