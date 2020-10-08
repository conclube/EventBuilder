package me.conclure.eventbuilder.implementation;

import com.google.common.collect.ImmutableList;
import me.conclure.eventbuilder.interfaces.EventHandler;
import me.conclure.eventbuilder.interfaces.EventSubscription;
import me.conclure.eventbuilder.internal.HandlerException;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

class EventHandlerImpl<T extends Event> implements EventHandler<T> {

    final List<Object> actionList;
    final List<Consumer<Exception>> exceptionList;
    final Class<T> eventType;

    EventPriority eventPriority;
    boolean ignoreCancelled;


    EventHandlerImpl(EventBuilderImpl<T> builder) {
        if (builder.actionList.isEmpty()) {
            throw new HandlerException("No actions defined");
        }
        actionList = ImmutableList.copyOf(builder.actionList);
        exceptionList = ImmutableList.copyOf(builder.exceptionList);
        eventType = builder.eventType;
        eventPriority = EventPriority.NORMAL;
    }

    @Override
    public EventSubscription<T> register(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        return new EventSubscriptionImpl<>(plugin,this);
    }

    @Override
    public EventHandler<T> ignoreCancelled(boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
        return this;
    }

    @Override
    public EventHandler<T> eventPriority(EventPriority eventPriority) {
        Objects.requireNonNull(eventPriority, "eventPriority");
        this.eventPriority = eventPriority;
        return this;
    }
}
