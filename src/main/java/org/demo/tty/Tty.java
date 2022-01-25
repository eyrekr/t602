package org.demo.tty;

import java.util.Arrays;

public final class Tty {
    private static final String CSI = "\033[";

    private final byte[] systemInBuffer = new byte[32];

    public Tty init() {
        restoreTtyStateOnExit()
                .rawMode()
                .hideCursor()
                .clearScreen();
        return this;
    }

    private Tty restoreTtyStateOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cookedMode().showCursor();
        }));
        return this;
    }

    public Ascii readNonBlocking() {
        try {
            if (System.in.available() > 0) {
                Arrays.fill(this.systemInBuffer, (byte) 0);
                System.in.read(this.systemInBuffer);
                return Ascii.from(this.systemInBuffer);
            }
            return Ascii.Nothing;
        } catch (Exception e) {
            return Ascii.Error;
        }
    }

    public int readNonBlockingRaw(final byte[] buffer) throws Exception {
        if (System.in.available() > 0) {
            return System.in.read(buffer);
        }
        return 0;
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

    public Tty clearScreen() {
        System.out.print("\033[2J");
        return this;
    }

    public Tty moveCursorToTopLeftCorner() {
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
    public Tty moveCursorTo(int column, int row) {
        System.out.print("\033[" + row + ";" + column + "H");
        return this;
    }

    public Tty hideCursor() {
        System.out.print("\033[?25l");
        return this;
    }

    public Tty showCursor() {
        System.out.print("\033[?25h");
        return this;
    }
}