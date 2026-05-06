package game.map;

/**
 * Central constant repository for all map-rendering characters.
 */
public final class PipePatterns {

    private PipePatterns() {
        // Non-instantiable utility class
    }

    /** Characters for filled pipes. */
    public static final class Filled {
        private Filled() {
            // Non-instantiable
        }

        public static final char UP_ARROW = '▼';
        public static final char DOWN_ARROW = '▲';
        public static final char LEFT_ARROW = '►';
        public static final char RIGHT_ARROW = '◄';
        public static final char HORIZONTAL = '═';
        public static final char VERTICAL = '║';
        public static final char TOP_LEFT = '╚';
        public static final char TOP_RIGHT = '╔';
        public static final char BOTTOM_LEFT = '╗';
        public static final char BOTTOM_RIGHT = '╝';
        public static final char CROSS = '╬';
    }

    /** Characters for unfilled pipes. */
    public static final class Unfilled {
        private Unfilled() {
            // Non-instantiable
        }

        public static final char UP_ARROW = 'v';
        public static final char DOWN_ARROW = '^';
        public static final char LEFT_ARROW = '<';
        public static final char RIGHT_ARROW = '>';
        public static final char HORIZONTAL = '-';
        public static final char VERTICAL = '|';
        public static final char TOP_LEFT = 'L';
        public static final char TOP_RIGHT = 'J';
        public static final char BOTTOM_LEFT = '7';
        public static final char BOTTOM_RIGHT = 'F';
        public static final char CROSS = '+';
    }

    /** Wall character. */
    public static final char WALL = '█';
}