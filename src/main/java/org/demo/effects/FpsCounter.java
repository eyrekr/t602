package org.demo.effects;

import org.demo.core.Layer;

public class FpsCounter implements Effect {

    private long lastTick = System.nanoTime();

    @Override
    public Lifecycle apply(final Layer layer) {
        final int timeToRenderOneFrameInMillis = (int) ((System.nanoTime() - lastTick) / 1_000_000L);
        lastTick = System.nanoTime();
        final int fps = timeToRenderOneFrameInMillis > 0 ? 1_000 / timeToRenderOneFrameInMillis : 0;
        final String text = fps +" fps";

        layer.put(layer.width - text.length(), 0, text);

        return Lifecycle.Active;
    }
}