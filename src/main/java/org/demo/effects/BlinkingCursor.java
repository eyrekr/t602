package org.demo.effects;

public class BlinkingCursor implements Effect {

    @Override
    public boolean apply(Layer layer) {
        boolean appears = (System.currentTimeMillis() / 500) % 2 == 0;
        if (appears) {
            layer.put('_');
        }
        return true;
    }

}