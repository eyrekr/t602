package org.demo.effects;

@FunctionalInterface
public interface Effect {
    /**
     * Do something to layer.
     *
     * @return false if the effect is no longer applicable and shall be removed
     */
    boolean apply(Layer layer);
}