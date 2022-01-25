package org.demo.core;

import org.demo.tty.Ascii;
import org.demo.tty.Tty;

import java.util.Arrays;

/**
 * Intended for probing values of keys;
 * press a key and see how it is encoded.
 */
public class InputProbe {

    private final Tty tty;

    public InputProbe(Tty tty) {
        this.tty = tty;
    }

    public void runUntil(final Ascii terminationKey) throws Exception {
        final byte[] bytes = new byte[32];

        while (true) {
            Arrays.fill(bytes, (byte) 0);
            tty.readNonBlockingRaw(bytes);
            for (byte value : bytes) {
                if (value > 0) {
                    System.out.printf("%x", value);
                }
            }

            final Ascii key = Ascii.from(bytes);
            System.out.printf("   char: %c   key: %s", key.character > 0 ? key.character : ' ', key);

            if (key == terminationKey) {
                break;
            }

            Thread.sleep(1L);
        }
    }
}
