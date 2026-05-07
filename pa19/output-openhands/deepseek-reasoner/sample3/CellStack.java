import java.util.Stack;

/**
 * Stack tracking placed pipes for undo functionality.
 */
public class CellStack {
    private final Stack<FillableCell> cellStack = new Stack<>();
    private int count;

    public void push(FillableCell cell) {
        cellStack.push(cell);
        count++;
    }

    public FillableCell pop() {
        if (cellStack.isEmpty()) {
            return null;
        }
        count--;
        return cellStack.pop();
    }

    public int getUndoCount() {
        return count;
    }

    public boolean isEmpty() {
        return cellStack.isEmpty();
    }

    public void display() {
        System.out.println("Undo Count: " + count);
    }
}
