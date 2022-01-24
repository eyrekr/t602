package org.demo.effects;

public class FpsCounter implements Effect {
    static final String DIGIT = "0123456789";

    long lastTick = System.nanoTime();

    @Override
    public boolean apply(Layer layer) {
        int ms = (int)((System.nanoTime() - lastTick) / 1_000_000L); // ms to render one frame
        int fps = ms > 0 ? 1_000 / ms : 0;
        lastTick = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            layer.put(79 - i, 24, DIGIT.charAt(fps % 10));
            fps /= 10;
        }
        return true;
    }
}