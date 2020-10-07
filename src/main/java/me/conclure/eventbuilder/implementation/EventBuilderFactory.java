package me.conclure.eventbuilder.implementation;

import me.conclure.eventbuilder.interfaces.EventBuilder;
import org.bukkit.event.Event;

import java.util.Objects;

public final class EventBuilderFactory {

    private EventBuilderFactory() { }

    public <T extends Event> EventBuilder<T> create(Class<T> eventType) {
        Objects.requireNonNull(eventType, "eventType");
        return new EventBuilderImpl<>(eventType);
    }

}
