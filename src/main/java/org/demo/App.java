package org.demo;

import org.demo.core.TextEditor;
import org.demo.tty.Tty;

public class App {

    public static void main(String[] args) throws Exception {
        final Tty tty = new Tty().init();

        //new InputProbe(tty).runUntil(Ascii.EndOfText);
        new TextEditor(tty).run();
    }
}
