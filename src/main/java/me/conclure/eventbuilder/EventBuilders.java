package me.conclure.eventbuilder;

import me.conclure.eventbuilder.implementation.EventBuilderFactory;
import me.conclure.eventbuilder.interfaces.EventBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

public final class EventBuilders {

    private static final EventBuilderFactory FACTORY;

    static {
        try {
            FACTORY = EventBuilderFactory.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Event> EventBuilder<T> create(Class<T> eventType) {
        return FACTORY.create(eventType);
    }

    public static <T extends Event> EventBuilder<T> create(Class<T> eventType,
                                             EventPriority eventPriority,
                                             boolean ignoreCancelled) {
        return FACTORY.create(eventType,eventPriority,ignoreCancelled);
    }

}
