package org.demo.effects;

import org.demo.core.Layer;

public class BlinkingCursor implements Effect {

    private final long delayInMs;
    private final char cursor;

    public BlinkingCursor(final char cursor, final long delayInMs) {
        assert (delayInMs > 10);
        this.cursor = cursor;
        this.delayInMs = delayInMs;
    }

    @Override
    public State apply(final Layer layer) {
        final boolean appears = (System.currentTimeMillis() / delayInMs) % 2 == 0;
        if (appears) {
            layer.put(cursor);
        }
        return State.Active;
    }
}