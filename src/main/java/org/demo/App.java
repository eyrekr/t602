package org.demo;

import org.demo.tty.Tty;

public class App {

    public static void main(String[] args) {
        final Tty tty = new Tty().init();

        //inputProbe();
        new TextEditor().start();
    }
}
