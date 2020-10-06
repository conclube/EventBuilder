package me.conclure.eventbuilder.interfaces;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EventBuilder<T extends Event> {

    EventBuilder<T> filter(Predicate<T> predicate);

    EventBuilder<T> execute(Consumer<T> consumer);

    EventBuilder<T> executeIf(Predicate<T> predicate,
                              Consumer<T> consumer,
                              boolean negated);

    EventBuilder<T> executeIf(Predicate<T> predicate,
                              Consumer<T> consumer);

    EventBuilder<T> onError(Consumer<Exception> consumer);

    EventBuilder<T> unregisterIf(Predicate<T> predicate);

    EventBuilder<T> ignoreCancelled(boolean ignoreCancelled);

    EventBuilder<T> eventPriority(EventPriority eventPriority);

    EventBuilder<T> restore();

    EventHandler<T> build();

    EventSubscription<T> register(Plugin plugin);
}
