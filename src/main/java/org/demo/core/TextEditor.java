package org.demo.core;

import org.demo.effects.*;
import org.demo.tty.Ascii;
import org.demo.tty.Tty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class TextEditor {

    private final Tty tty;
    private final Layer baseLayer = new Layer(80, 25);
    private final Layer effectsLayer = new Layer(80, 25);
    private final Layer lastRenderedLayer = new Layer(80, 25);
    private final LinkedList<Effect> effects = new LinkedList<>();

    private boolean quit = false;
    private Runnable keyboardInputHandler = this::handleStandardKeyboardInput;

    private enum Mode {
        Standard, Table
    }

    public TextEditor(Tty tty) {
        this.tty = tty;
        effects.add(new RowColumnInfo());
        effects.add(new BlinkingCursor(500));
    }

    public void run() throws Exception {
        while (!quit) {
            keyboardInputHandler.run();
            applyEffects();
            render();
            Thread.sleep(10L);
        }
    }

    void handleStandardKeyboardInput() {
        final Ascii key = tty.readNonBlocking();
        if (key != Ascii.Nothing) {
            switch (key) {
                case ArrowUp -> this.baseLayer.moveCursorBy(0, -1);
                case ArrowDown -> this.baseLayer.moveCursorBy(0, +1);
                case ArrowLeft -> this.baseLayer.moveCursorBy(-1, 0);
                case ArrowRight -> this.baseLayer.moveCursorBy(+1, 0);
                case Enter -> this.baseLayer.setColumn(0).moveCursorBy(0, +1);
                case Backspace -> this.baseLayer.moveCursorBy(-1, 0).put('\0');
                case EndOfText, EndOfTransmission, Escape -> this.quit = true;
                case F5 -> printScreen();
                case F6 -> switchToTableMode();
                default -> {
                    if (key.character > 0) {
                        this.baseLayer.put(key.character).moveCursorBy(+1, 0);
                    }
                }
            }
        }
    }

    void handleTableKeyboardInput() {
        final Ascii key = tty.readNonBlocking();
        if (key != Ascii.Nothing) {
            switch (key) {
                case ArrowUp -> this.baseLayer.moveCursorBy(0, -1);
                case ArrowDown -> this.baseLayer.moveCursorBy(0, +1);
                case ArrowLeft -> this.baseLayer.moveCursorBy(-1, 0);
                case ArrowRight -> this.baseLayer.moveCursorBy(+1, 0);
                case Enter -> this.baseLayer.setColumn(0).moveCursorBy(0, +1);
                case Backspace -> this.baseLayer.moveCursorBy(-1, 0).put('\0');
                case EndOfText, EndOfTransmission, Escape -> this.quit = true;
                case F5 -> printScreen();
                case F6 -> switchToStandardMode();
                default -> {
                    // do nothing
                }
            }
        }
    }

    void switchToTableMode() {
        this.keyboardInputHandler = this::handleTableKeyboardInput;
    }

    void switchToStandardMode() {
        this.keyboardInputHandler = this::handleStandardKeyboardInput;
    }

    void applyEffects() {
        effectsLayer.becomeExactCopyOf(baseLayer);
        for (var iterator = effects.iterator(); iterator.hasNext(); ) {
            final Effect effect = iterator.next();
            final Effect.State state = effect.apply(effectsLayer);
            if (state == Effect.State.Finished) {
                iterator.remove();
            }
        }
    }

    void render() {
        if (effectsLayer.isInTheSameStateAs(lastRenderedLayer)) {
            return;
        }

        tty.moveCursorToTopLeftCorner();
        for (int row = 0; row < effectsLayer.height; row++) {
            for (int column = 0; column < effectsLayer.width; column++) {
                final char ch = effectsLayer.get(column, row);
                System.out.print(ch == Ascii.Null.character ? ' ' : ch);
            }
            System.out.print("\r\n");
        }

        lastRenderedLayer.becomeExactCopyOf(effectsLayer);
    }

    void printScreen() {
        final Path path = Paths.get("target/screenshot.txt");
        try {
            Files.writeString(
                    path,
                    baseLayer.toString(),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (final Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
