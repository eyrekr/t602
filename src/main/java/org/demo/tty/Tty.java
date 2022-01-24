package org.demo.tty;

import java.util.Arrays;

public class Tty {
    private static final String CSI = "\033[";
    private final byte[] buffer = new byte[32];

    public Tty init() {
        installShutdownHook()
                .rawMode()
                .hideCursor()
                .clearScreen();
        return this;
    }

    /**
     * Restore the terminal state on exit.
     */
    private Tty installShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cookedMode().showCursor();
        }));
        return this;
    }

    public Ascii readNonBlocking() {
        try {
            if (System.in.available() > 0) {
                Arrays.fill(buffer, (byte) 0);
                System.in.read(buffer);
                return Ascii.from(buffer);
            }
            return Ascii.Nothing;
        } catch (Exception e) {
            return Ascii.Error;
        }
    }

    public Tty render(final int width, final int height, final char[] buffer) {
        moveCursorToTopLeftCorner();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                final int i = width * row + column;
                final char ch = buffer[i];
                System.out.print(ch == Ascii.Null.character ? ' ' : ch);
            }
            System.out.print("\r\n");
        }
        return this;
    }

    private Tty rawMode() {
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo </dev/tty"});
        } catch (Exception e) {
        }
        return this;
    }

    private Tty cookedMode() {
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty -raw echo </dev/tty"});
        } catch (Exception e) {
        }
        return this;
    }

    // static Size size() {
    //     try {
    //         String signals = "\033[s\033[5000;5000H\033[6n\033[u";
    //         System.out.print(signals);
    //         byte[] bytes = new byte[32];
    //         int bytesRead = System.in.read(bytes);

    //         for (int i = 0; i < bytesRead; i++) {
    //             System.out.printf("%x", bytes[i]);
    //             // 1b 5b 34 31 3b 31 37 39 52
    //             // ^[41;179R
    //         }
    //         System.out.print("\r\n");
    //     } catch(Exception e) {
    //     }
    //     return new Size(10,10);
    // }

    private Tty clearScreen() {
        System.out.print("\033[2J");
        return this;
    }

    private Tty moveCursorToTopLeftCorner() {
        System.out.print("\033[H");
        return this;
    }

    /**
     * Moves the cursor to row n, column m.
     * The values are 1-based, and default to 1 (top left corner) if omitted.
     * A sequence such as CSI ;5H is a synonym for CSI 1;5H as well as CSI 17;H is the same as CSI 17H and CSI 17;1H
     *
     * @param column column, 1-based
     * @param row    row, 1-based
     */
    private Tty moveCursorTo(int column, int row) {
        System.out.print("\033[" + row + ";" + column + "H");
        return this;
    }

    private Tty hideCursor() {
        System.out.print("\033[?25l");
        return this;
    }

    private Tty showCursor() {
        System.out.print("\033[?25h");
        return this;
    }

    private static char firstNotNull(char... characters) {
        for (char ch : characters) {
            if (ch != Ascii.Null.character) {
                return ch;
            }
        }
        return ' ';
    }
}