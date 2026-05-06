package game.map;

/**
 * Represents the delay countdown bar for water flow.
 */
public class DelayBar {

    private final int initialValue;
    private int currentValue;

    public DelayBar() {
        this.initialValue = 0;
        this.currentValue = 0;
    }

    public DelayBar(int initialValue) {
        this.initialValue = initialValue;
        this.currentValue = initialValue;
    }

    /**
     * Decrements the current value by 1 each round.
     */
    public void countdown() {
        if (currentValue > 0) {
            currentValue--;
        }
    }

    /**
     * Returns -currentValue, representing how far the water should flow.
     * Negative during countdown, positive after delay ends.
     *
     * @return The distance value.
     */
    public int distance() {
        return -currentValue;
    }

    /**
     * Displays the delay bar.
     */
    public void display() {
        if (currentValue > 0) {
            System.out.print("Rounds Countdown: ");
            System.out.print(StringUtils.createPadding(currentValue, '='));
            System.out.print(StringUtils.createPadding(initialValue - currentValue, ' '));
            System.out.print(" " + currentValue);
            System.out.println();
        } else {
            System.out.println();
        }
    }

    /**
     * Gets the current value.
     *
     * @return The current value.
     */
    public int getCurrentValue() {
        return currentValue;
    }
}