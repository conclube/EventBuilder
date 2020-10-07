package me.conclure.eventbuilder;

import me.conclure.eventbuilder.implementation.EventBuilderFactory;
import me.conclure.eventbuilder.interfaces.EventBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

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

}
