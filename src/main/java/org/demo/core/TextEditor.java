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

    public TextEditor(Tty tty) {
        this.tty = tty;
        //effects.add(new RandomDent());
        effects.add(new DashingLine());
        effects.add(new FpsCounter());
        effects.add(new RowColumnInfo());
        effects.add(new BlinkingCursor(500));
    }

    public void run() throws Exception {
        while (!quit) {
            handleKeyboardInput();
            applyEffects();
            render();
            Thread.sleep(10L);
        }
    }

    void handleKeyboardInput() {
        final Ascii key = tty.readNonBlocking();
        if (key != Ascii.Nothing) {
            switch (key) {
                case ArrowUp:
                    baseLayer.moveCursorBy(0, -1);
                    break;
                case ArrowDown:
                    baseLayer.moveCursorBy(0, +1);
                    break;
                case ArrowLeft:
                    baseLayer.moveCursorBy(-1, 0);
                    break;
                case ArrowRight:
                    baseLayer.moveCursorBy(+1, 0);
                    break;
                case Enter:
                    baseLayer.setColumn(0).moveCursorBy(0, +1);
                    break;
                case Backspace:
                    baseLayer.moveCursorBy(-1, 0).put('\0');
                    break;
                case EndOfText:
                case EndOfTransmission:
                    quit = true;
                    break;
                case F5:
                    printScreen();
                    break;
                default:
                    if (key.character > 0) {
                        effects.add(new ChaoticCharacter(baseLayer.getColumn(), baseLayer.getRow(), 2));
                        baseLayer.put(key.character).moveCursorBy(+1, 0);
                    }
            }
        }
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
