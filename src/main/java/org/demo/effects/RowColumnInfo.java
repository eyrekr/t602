package org.demo.effects;

import org.demo.core.Layer;

public class RowColumnInfo implements Effect {

    @Override
    public State apply(final Layer layer) {
        final String info = (layer.getRow() + 1) + ":" + (layer.getColumn() + 1);
        layer.put(0, layer.height - 1, info);
        return State.Active;
    }
}
