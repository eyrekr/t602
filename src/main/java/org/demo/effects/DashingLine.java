package org.demo.effects;

import org.demo.core.Layer;

public class DashingLine implements Effect {

    private int magnifiedColumn = 0;
    private long timestamp = System.currentTimeMillis();

    @Override
    public Lifecycle apply(final Layer layer) {
        final int row = layer.getRow();
        final char ch = layer.get(this.magnifiedColumn, row);
        if (Character.isLowerCase(ch)) {
            layer.put(this.magnifiedColumn, row, Character.toUpperCase(ch));
        }
        if (System.currentTimeMillis() - this.timestamp > 50) {
            this.magnifiedColumn = (this.magnifiedColumn + 1) % layer.width;
            this.timestamp = System.currentTimeMillis();
        }
        return Lifecycle.Active;
    }
}
