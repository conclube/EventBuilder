package me.conclure.eventbuilder.implementation;

import me.conclure.eventbuilder.interfaces.EventHandler;
import me.conclure.eventbuilder.interfaces.EventSubscription;
import me.conclure.eventbuilder.internal.HandlerException;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

class EventHandlerImpl<T extends Event> implements EventHandler<T> {

    EventBuilderImpl<T> builder;
    boolean isValid;

    EventHandlerImpl(EventBuilderImpl<T> builder) {
        if (builder.actionList.isEmpty()) {
            throw new HandlerException("No handlers");
        }
        this.builder = builder;
        isValid = true;
    }

    @Override
    public EventSubscription<T> register(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        validate();
        return new EventSubscriptionImpl<>(plugin,this);
    }

    @Override
    public EventHandler<T> ignoreCancelled(boolean ignoreCancelled) {
        validate();
        builder.ignoreCancelled(ignoreCancelled);
        return this;
    }

    @Override
    public EventHandler<T> eventPriority(EventPriority eventPriority) {
        Objects.requireNonNull(eventPriority, "eventPriority");
        validate();
        builder.eventPriority(eventPriority);
        return this;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public boolean invalidate() {
        if (!isValid && builder == null) {
            return false;
        }
        builder = null;
        isValid = false;
        return true;
    }

    void validate() {
        if (!isValid) throw new HandlerException("Invalid EventHandler");
    }
}
