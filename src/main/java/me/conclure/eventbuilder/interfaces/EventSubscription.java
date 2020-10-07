package me.conclure.eventbuilder.interfaces;

import com.sun.istack.internal.NotNull;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

/**
 * @author Conclure
 * @since 1.0.0
 * @param <T> event type
 */
public interface EventSubscription<T extends Event> {

    /**
     * Gets whether this subscription still is registered.
     * <br>
     * If the invocation of this method returns {@code true}, you
     * may invoke {@link EventSubscription#unregister()} which will
     * by highest chance return {@code true}. Albeit this method
     * depends on an {@link java.util.concurrent.atomic.AtomicBoolean}
     * making it not safe for concurrent operations as it might be
     * changed asynchronously.
     *
     * @return registered status
     */
    boolean isRegistered();

    /**
     * Gets the event priority of the subscription.
     *
     * @return {@link EventPriority}
     */
    @NotNull
    EventPriority getPriority();

    /**
     * Gets whether the subscription is ignoring cancelled events or not.
     *
     * @return status of ignore cancelled events
     */
    boolean ignoreCancelled();

    /**
     * Gets the event type of the subscription.
     *
     * @return event class
     */
    @NotNull
    Class<T> getEventType();

    /**
     * Gets the owner plugin of the subscription.
     *
     * @return owner
     */
    @NotNull
    Plugin getOwner();

    /**
     * Tries to unregister the subscription.
     * <br>
     * This will return {@code true} only if the invocation of this method
     * was successful which means that the subscription was unregistered by
     * the invoker. It will return {@code false} if the subscription is
     * already unregistered.
     *
     * @return status of successfully unregistering
     */
    boolean unregister();

    /**
     * Tries to register the subscription.
     * <br>
     * This will return {@code true} only if the invocation of this method
     * was successful which means that the subscription was registered by
     * the invoker. It will return {@code false} if the subscription is
     * already registered.
     *
     * @return status of successfully registering
     */
    boolean register();

}
