package org.demo.effects;

import org.demo.core.Layer;

@FunctionalInterface
public interface Effect {

    Lifecycle apply(final Layer layer);

    enum Lifecycle {
        Active, Finished
    }
}