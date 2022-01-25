package org.demo.core;

import org.demo.tty.Ascii;
import org.demo.tty.Tty;

/**
 * Intended for probing values of keys;
 * press a key and see how it is encoded.
 */
public class InputProbe {

    public void inputProbe() throws Exception {
        while (true) {
            Ascii key = Tty.readNonBlocking();
            if (key != Ascii.Nothing) {
                Tty.clearScreen();

                for (byte value : Tty.buffer) {
                    if (value > 0) {
                        System.out.printf("%x", value);
                    }
                }

                System.out.printf("\r\n%c   %s", key.character > 0 ? key.character : ' ', key);

                if (key == Ascii.EndOfText || key == Ascii.EndOfTransmission) {
                    System.exit(0);
                }
            }
            Thread.sleep(1L);
        }
    }
}
