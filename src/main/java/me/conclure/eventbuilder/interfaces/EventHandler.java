package me.conclure.eventbuilder.interfaces;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

public interface EventHandler<T extends Event> {

    EventSubscription<T> register(Plugin plugin);

    EventHandler<T> ignoreCancelled(boolean ignoreCancelled);

    EventHandler<T> eventPriority(EventPriority eventPriority);
}
