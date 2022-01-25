package org.demo.effects;

import org.demo.core.Layer;

import java.util.Random;

public class ChaoticCharacter implements Effect {
    static final String PALETTE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*_+";

    final int column;
    final int row;
    final int count;
    final Random random = new Random();
    int i = 1;

    public ChaoticCharacter(int column, int row, int count) {
        this.column = column;
        this.row = row;
        this.count = count;
    }

    @Override
    public Lifecycle apply(final Layer layer) {
        if (i > count) {
            return Lifecycle.Finished;
        }
        layer.put(column, row, randomCharacter());
        i += 1;
        return Lifecycle.Active;
    }

    char randomCharacter() {
        return PALETTE.charAt(random.nextInt(PALETTE.length()));
    }
}