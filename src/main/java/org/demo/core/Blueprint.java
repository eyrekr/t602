package org.demo.core;

public class Blueprint {

    public enum Direction {
        Left(2, 8, -1, 0),
        Right(8, 2, +1, 0),
        Up(1, 4, 0, -1),
        Down(4, 1, 0, +1);

        private final int mask1;
        private final int mask2;
        private final int columnDelta;
        private final int rowDelta;

        Direction(final int mask1, final int mask2, final int columnDelta, final int rowDelta) {
            this.mask1 = mask1;
            this.mask2 = mask2;
            this.columnDelta = columnDelta;
            this.rowDelta = rowDelta;
        }
    }

    public enum Mode {
        Draw, Erase
    }

    private static final String GLYPHS = " ╵╴┘╷│┐┤╶└─┴┌├┬┼";
    private final Layer layer;
    private Mode mode = Mode.Draw;

    public Blueprint(final Layer layer) {
        this.layer = layer;
    }

    public Blueprint setMode(final Mode mode) {
        this.mode = mode;
        return this;
    }

    public Blueprint move(final Direction direction) {
        final char c1 = update(this.layer.get(), direction.mask1);
        this.layer.put(c1);

        this.layer.moveCursorBy(direction.columnDelta, direction.rowDelta);

        final char c2 = update(this.layer.get(), direction.mask2);
        this.layer.put(c2);

        return this;
    }

    private char update(final char glyph, final int bit) {
        final int code = Math.max(GLYPHS.indexOf(glyph), 0);
        return switch (this.mode) {
            case Draw -> GLYPHS.charAt(code | bit);
            case Erase -> GLYPHS.charAt(code & (~bit));
        };
    }
}
