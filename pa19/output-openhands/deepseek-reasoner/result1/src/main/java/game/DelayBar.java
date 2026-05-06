package game;

import game.util.StringUtils;

/**
 * Counts down rounds before water starts flowing, then tracks flow distance.
 */
public class DelayBar {

    private final int initialValue;
    private int currentValue;

    /**
     * Constructs a DelayBar with the given initial value.
     *
     * @param initialValue initial countdown value
     */
    public DelayBar(int initialValue) {
        this.initialValue = initialValue;
        this.currentValue = initialValue;
    }

    /**
     * Decrements the current value by 1 each round.
     */
    public void countdown() {
        currentValue--;
    }

    /**
     * Returns the current distance for water flow.
     * Negative during countdown, positive after delay ends.
     *
     * @return distance value
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
}
