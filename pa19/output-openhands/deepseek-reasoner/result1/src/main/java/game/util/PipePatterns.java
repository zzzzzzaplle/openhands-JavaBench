package game.util;

/**
 * Central constant repository for all map-rendering characters.
 */
public final class PipePatterns {

    /** Wall character. */
    public static final char WALL = '#';

    private PipePatterns() {
        // prevent instantiation
    }

    /**
     * Characters for filled (water-flowing) pipes and arrows.
     */
    public static final class Filled {
        public static final char UP_ARROW = '⇑';
        public static final char DOWN_ARROW = '⇓';
        public static final char LEFT_ARROW = '⇐';
        public static final char RIGHT_ARROW = '⇒';
        public static final char HORIZONTAL = '═';
        public static final char VERTICAL = '║';
        public static final char TOP_LEFT = '╔';
        public static final char TOP_RIGHT = '╗';
        public static final char BOTTOM_LEFT = '╚';
        public static final char BOTTOM_RIGHT = '╝';
        public static final char CROSS = '╬';

        private Filled() {
        }
    }

    /**
     * Characters for unfilled (dry) pipes and arrows.
     */
    public static final class Unfilled {
        public static final char UP_ARROW = '↑';
        public static final char DOWN_ARROW = '↓';
        public static final char LEFT_ARROW = '←';
        public static final char RIGHT_ARROW = '→';
        public static final char HORIZONTAL = '─';
        public static final char VERTICAL = '│';
        public static final char TOP_LEFT = '┌';
        public static final char TOP_RIGHT = '┐';
        public static final char BOTTOM_LEFT = '└';
        public static final char BOTTOM_RIGHT = '┘';
        public static final char CROSS = '┼';

        private Unfilled() {
        }
    }
}
