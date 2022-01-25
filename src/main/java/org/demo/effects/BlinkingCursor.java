package org.demo.effects;

import org.demo.core.Layer;

public class BlinkingCursor implements Effect {

    private final long delayInMs;

    public BlinkingCursor(long delayInMs) {
        assert (delayInMs > 10);
        this.delayInMs = delayInMs;
    }

    @Override
    public State apply(final Layer layer) {
        final boolean appears = (System.currentTimeMillis() / delayInMs) % 2 == 0;
        if (appears) {
            layer.put('_');
        }
        return State.Active;
    }
}