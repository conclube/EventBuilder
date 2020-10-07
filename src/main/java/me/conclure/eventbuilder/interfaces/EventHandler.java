package me.conclure.eventbuilder.interfaces;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Conclure
 * @since 1.0.0
 * @param <T> event type
 */
public interface EventHandler<T extends Event> {

    /**
     * Returns an {@link EventSubscription} based of the handler and it's
     * builder.
     * <br>
     * When invoking this method, it will implicitly create a
     * {@link EventSubscription} which will be registered. You can still
     * save an instance of the builder and invoke this method again to
     * receive a new instance of an {@link EventSubscription} based of the
     * handler and it's builder.
     *
     * @param plugin owner
     *
     * @return {@link EventSubscription}
     */
    @NotNull
    EventSubscription<T> register(@NotNull Plugin plugin);

    /**
     * Sets whether the handler should ignore cancelled events or not.
     * It will be {@code false} by default.
     *
     * @param ignoreCancelled status of ignore cancelled events
     *
     * @return same instance
     */
    @NotNull
    EventHandler<T> ignoreCancelled(boolean ignoreCancelled);

    /**
     * Sets the event priority of the handler. It will be
     * {@link EventPriority#NORMAL} by default.
     *
     * @param eventPriority event priority
     *
     * @return same instance
     */
    @NotNull
    EventHandler<T> eventPriority(@NotNull EventPriority eventPriority);
}
