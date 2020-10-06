package me.conclure.eventbuilder.internal;

import java.util.function.Predicate;

public class UnregisterPredicate<T> {
    private final Predicate<T> predicate;

    public UnregisterPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }
}
