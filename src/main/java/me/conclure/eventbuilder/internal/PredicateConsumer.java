package me.conclure.eventbuilder.internal;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredicateConsumer<T> {

    private final Predicate<T> predicate;
    private final Consumer<T> consumer;

    public PredicateConsumer(Predicate<T> predicate,
                             Consumer<T> consumer,
                             boolean negated) {
        this.predicate = negated ? predicate.negate() : predicate;
        this.consumer = consumer;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public Consumer<T> getConsumer() {
        return consumer;
    }

    public static class IfElse<T> {

        private final Predicate<T> predicate;
        private final Consumer<T> ifConsumer;
        private final Consumer<T> elseConsumer;

        public IfElse(Predicate<T> predicate,
                      Consumer<T> ifConsumer,
                      Consumer<T> elseConsumer) {
            this.predicate = predicate;
            this.ifConsumer = ifConsumer;
            this.elseConsumer = elseConsumer;
        }

        public Consumer<T> getIfConsumer() {
            return ifConsumer;
        }

        public Consumer<T> getElseConsumer() {
            return elseConsumer;
        }

        public Predicate<T> getPredicate() {
            return predicate;
        }
    }

    public static class Filter<T> {

        private final Predicate<T> predicate;
        private final Consumer<T> consumer;

        public Filter(Predicate<T> predicate,
                      Consumer<T> consumer) {
            this.predicate = predicate;
            this.consumer = consumer;
        }

        public Predicate<T> getPredicate() {
            return predicate;
        }

        public Consumer<T> getConsumer() {
            return consumer;
        }
    }
}
