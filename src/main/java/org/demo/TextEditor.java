package org.demo;

public class TextEditor {
    Layer layer = new Layer(80, 25);
    Layer overlay = new Layer(80, 25); // for animation magic
    boolean quit = false;

    LinkedList<Effect> effects = new LinkedList<>();

    void handleKeyboardInput() {
        Ascii key = Console.readNonBlocking();
        if (key != Ascii.Nothing) {
            layer.dirty = true;
            switch (key) {
                case ArrowUp:
                    layer.move(0, -1);
                    break;
                case ArrowDown:
                    layer.move(0, +1);
                    break;
                case ArrowLeft:
                    layer.move(-1, 0);
                    break;
                case ArrowRight:
                    layer.move(+1, 0);
                    break;
                case Enter:
                    layer.column(0).move(0, +1);
                    break;
                case Backspace:
                    layer.move(-1, 0).put('\0');
                    break;
                case EndOfText:
                case EndOfTransmission:
                    quit = true;
                    break;
                default:
                    if (key.character > 0) {
                        effects.add(new ChaoticCharacter(layer.column, layer.row, 3));
                        layer.put(key.character).move(+1, 0);
                    } else {
                        layer.dirty = false;
                    }
            }
        }
    }

    void applyEffects() {
        overlay.erase().column(layer.column).row(layer.row);
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
                Console.render(layer, overlay);
                layer.dirty = false;
                overlay.dirty = false;
            }
            Thread.sleep(10L);
        }
    }

}
