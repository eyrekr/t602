package org.demo.effects;

import org.demo.core.Layer;

public class RowColumnInfo implements Effect {

    @Override
    public Lifecycle apply(final Layer layer) {
        final String info = layer.getRow() + ":" + layer.getColumn();
        layer.put(0, layer.height - 1, info);
        return Lifecycle.Active;
    }
}
