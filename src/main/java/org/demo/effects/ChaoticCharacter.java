package org.demo.effects;

import org.demo.core.Layer;

import java.util.Random;

public class ChaoticCharacter implements Effect {
    private static final String PALETTE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*_+";

    private final int column;
    private final int row;
    private final int count;
    private final Random random = new Random();
    private int i = 1;

    public ChaoticCharacter(int column, int row, int count) {
        this.column = column;
        this.row = row;
        this.count = count;
    }

    @Override
    public State apply(final Layer layer) {
        if (i > count) {
            return State.Finished;
        }
        layer.put(column, row, randomCharacter());
        i += 1;
        return State.Active;
    }

    char randomCharacter() {
        return PALETTE.charAt(random.nextInt(PALETTE.length()));
    }
}