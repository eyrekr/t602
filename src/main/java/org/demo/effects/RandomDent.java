package org.demo.effects;

import org.demo.core.Layer;

import java.util.Random;

public class RandomDent implements Effect {

    private final Random random = new Random();

    @Override
    public State apply(final Layer layer) {
        if (random.nextInt(100) == 0) {
            final int randomColumn = random.nextInt(layer.width);
            final int randomRow = random.nextInt(layer.height);
            layer.put(randomColumn, randomRow, '#');
        }
        return State.Active;
    }
}
