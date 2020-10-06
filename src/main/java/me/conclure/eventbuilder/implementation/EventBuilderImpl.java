package me.conclure.eventbuilder.implementation;

import me.conclure.eventbuilder.interfaces.EventBuilder;
import me.conclure.eventbuilder.interfaces.EventHandler;
import me.conclure.eventbuilder.interfaces.EventSubscription;
import me.conclure.eventbuilder.internal.PredicateConsumer;
import me.conclure.eventbuilder.internal.UnregisterPredicate;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

class EventBuilderImpl<T extends Event> implements EventBuilder<T> {

    final List<Object> actionList;
    final List<Consumer<Exception>> exceptionList;
    final Class<T> eventType;

    EventPriority eventPriority;
    boolean ignoreCancelled;

    EventBuilderImpl(Class<T> eventType,
                     EventPriority eventPriority,
                     boolean ignoreCancelled) {
        actionList = new ArrayList<>();
        exceptionList = new ArrayList<>();
        this.eventType = eventType;
        this.eventPriority = eventPriority;
        this.ignoreCancelled = ignoreCancelled;
    }

    @Override
    public EventBuilder<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        actionList.add(predicate);
        return this;
    }

    @Override
    public EventBuilder<T> execute(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        actionList.add(consumer);
        return this;
    }

    @Override
    public EventBuilder<T> executeIf(Predicate<T> predicate,
                                     Consumer<T> consumer,
                                     boolean negated) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(consumer, "consumer");
        actionList.add(new PredicateConsumer<>(predicate,consumer,negated));
        return this;
    }

    @Override
    public EventBuilder<T> executeIf(Predicate<T> predicate,
                                     Consumer<T> consumer) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(consumer, "consumer");
        actionList.add(new PredicateConsumer<>(predicate,consumer,false));
        return this;
    }

    @Override
    public EventBuilder<T> onError(Consumer<Exception> exceptionConsumer) {
        Objects.requireNonNull(exceptionConsumer, "consumer");
        exceptionList.add(exceptionConsumer);
        return this;
    }

    @Override
    public EventBuilder<T> unregisterIf(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        actionList.add(new UnregisterPredicate<>(predicate));
        return this;
    }

    @Override
    public EventBuilder<T> ignoreCancelled(boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
        return this;
    }

    @Override
    public EventBuilder<T> eventPriority(EventPriority eventPriority) {
        Objects.requireNonNull(eventPriority, "eventPriority");
        this.eventPriority = eventPriority;
        return this;
    }

    @Override
    public EventBuilder<T> restore() {
        actionList.clear();
        exceptionList.clear();
        return new EventBuilderImpl<>(eventType,eventPriority,ignoreCancelled);
    }

    @Override
    public EventHandler<T> build() {
        return new EventHandlerImpl<>(this);
    }

    @Override
    public EventSubscription<T> register(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        return build().register(plugin);
    }
}
