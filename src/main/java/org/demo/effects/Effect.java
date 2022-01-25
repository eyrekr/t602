package org.demo.effects;

import org.demo.core.Layer;

@FunctionalInterface
public interface Effect {

    State apply(final Layer layer);

    enum State {
        Active, Finished
    }
}