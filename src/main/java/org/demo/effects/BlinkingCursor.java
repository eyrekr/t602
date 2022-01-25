package org.demo.effects;

import org.demo.core.Layer;

public class BlinkingCursor implements Effect {

    @Override
    public Lifecycle apply(final Layer layer) {
        final boolean appears = (System.currentTimeMillis() / 100) % 2 == 0;
        if (appears) {
            layer.put('_');
        }
        return Lifecycle.Active;
    }
}