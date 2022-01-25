package org.demo.core;

import org.demo.effects.ChaoticCharacter;
import org.demo.effects.Effect;
import org.demo.tty.Ascii;
import org.demo.tty.Tty;

import java.util.LinkedList;

public class TextEditor {

    private final Tty tty;
    private final Layer layer = new Layer(80, 25);
    private final Layer overlay = new Layer(80, 25); // for animation magic
    private final LinkedList<Effect> effects = new LinkedList<>();

    private boolean quit = false;

    public TextEditor(Tty tty) {
        this.tty = tty;
    }

    void handleKeyboardInput() {
        final Ascii key = tty.readNonBlocking();
        if (key != Ascii.Nothing) {
            layer.dirty = true;
            switch (key) {
                case ArrowUp:
                    layer.moveCursorBy(0, -1);
                    break;
                case ArrowDown:
                    layer.moveCursorBy(0, +1);
                    break;
                case ArrowLeft:
                    layer.moveCursorBy(-1, 0);
                    break;
                case ArrowRight:
                    layer.moveCursorBy(+1, 0);
                    break;
                case Enter:
                    layer.column(0).move(0, +1);
                    break;
                case Backspace:
                    layer.moveCursorBy(-1, 0).put('\0');
                    break;
                case EndOfText:
                case EndOfTransmission:
                    quit = true;
                    break;
                default:
                    if (key.character > 0) {
                        effects.add(new ChaoticCharacter(layer.column, layer.row, 3));
                        layer.put(key.character).moveCursorBy(+1, 0);
                    } else {
                        layer.dirty = false;
                    }
            }
        }
    }

    void applyEffects() {
        overlay.reset().column(layer.column).row(layer.row);
        for (var iterator = effects.iterator(); iterator.hasNext(); ) {
            Effect effect = iterator.next();
            if (!effect.apply(overlay)) {
                iterator.remove();
            }
        }
    }

    void start() throws Exception {
        effects.add(new BlinkingCursor());
        //effects.add(new FpsCounter());
        while (!quit) {
            handleKeyboardInput();
            applyEffects();
            if (layer.dirty || overlay.dirty) {
                tty.render(layer, overlay);
                layer.dirty = false;
                overlay.dirty = false;
            }
            Thread.sleep(10L);
        }
    }

}
