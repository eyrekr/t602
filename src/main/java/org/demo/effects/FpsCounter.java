package org.demo.effects;

import org.demo.core.Layer;

public class FpsCounter implements Effect {

    private long lastTime = System.nanoTime();
    private int[] durations = new int[32];
    private int i = 0;

    @Override
    public Lifecycle apply(final Layer layer) {
        final int durationInMillis = (int) ((System.nanoTime() - this.lastTime) / 1_000_000L);
        this.lastTime = System.nanoTime();
        this.durations[this.i] = durationInMillis;
        this.i = (this.i + 1) % this.durations.length;

        int sum = 0;
        for (int i = 0; i < this.durations.length; i++) {
            sum += this.durations[i];
        }
        final String text = (sum / this.durations.length) + "ms";
        layer.put(layer.width - text.length(), 0, text);

        return Lifecycle.Active;
    }
}