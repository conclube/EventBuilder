package me.conclure.eventbuilder.interfaces;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

public interface EventSubscription<T extends Event> {

    boolean isRegistered();

    EventPriority getPriority();

    boolean ignoreCancelled();

    Class<T> getEventType();

    Plugin getOwner();

    boolean unregister();

    boolean register();

}
