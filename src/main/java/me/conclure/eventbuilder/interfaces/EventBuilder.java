package me.conclure.eventbuilder.interfaces;

import org.jetbrains.annotations.NotNull;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Conclure
 * @since 1.0.0
 * @param <T> event type
 */
public interface EventBuilder<T extends Event> {

    /**
     * Applies a filter to the builder.
     * <br>
     * When the {@link Predicate#test(Object)} returns {@code true}
     * any code under it will be executed whereas if would return
     * {@code false} would stop any further code execution.
     *
     * @param predicate filter
     *
     * @return same instance
     */
    @NotNull
    EventBuilder<T> filter(@NotNull Predicate<T> predicate);

    /**
     * Adds an action to the builder.
     *
     * @param consumer action
     *
     * @return same instance
     */
    @NotNull
    EventBuilder<T> execute(@NotNull Consumer<T> consumer);

    /**
     * Adds an action to the builder.
     * <br>
     * {@link Consumer#accept(Object)} will only be executed if
     * {@link Predicate#test(Object)} returns {@code true}. Any filters
     * above will still apply.
     *
     * @param predicate filter
     * @param consumer action
     * @param negated negated predicate
     *
     * @return same instance
     */
    @NotNull
    EventBuilder<T> executeIf(@NotNull Predicate<T> predicate,
                              @NotNull Consumer<T> consumer,
                              boolean negated);

    /**
     * Adds an action to the builder.
     * <br>
     * {@link Consumer#accept(Object)} will only be executed if
     * {@link Predicate#test(Object)} returns {@code true}. Any filters
     * above will still apply.
     *
     * @param predicate filter
     * @param consumer action
     *
     * @return same instance
     */
    @NotNull
    EventBuilder<T> executeIf(@NotNull Predicate<T> predicate,
                              @NotNull Consumer<T> consumer);

    /**
     * Adds an action for any exception to the builder.
     * <br>
     * The {@link Consumer#accept(Object)} will be called regardless
     * any filter if an {@link Exception} is caught.
     *
     * @param consumer error action
     *
     * @return same instance
     */
    @NotNull
    EventBuilder<T> onError(@NotNull Consumer<Exception> consumer);

    /**
     * Adds a filter that to the builder which will unregister the
     * {@link EventSubscription}.
     * <br>
     * If the {@link Predicate#test(Object)} returns {@code true} the
     * {@link EventSubscription#unregister()} will be called. This will
     * cause the {@link EventSubscription} to stop listening at any
     * future events. But it will still function the same time as the
     * {@link Predicate} is reached. Any filters above will still apply.
     *
     * @param predicate filter
     *
     * @return same instance
     */
    @NotNull
    EventBuilder<T> unregisterIf(@NotNull Predicate<T> predicate);

    /**
     * Clears the current builder and returns a new instance.
     *
     * @return new instance
     */
    @NotNull
    EventBuilder<T> restore();

    /**
     * Returns an {@link EventHandler} based of the builder.
     * <br>
     * No modifications to any actions or filters is allowed once this
     * method is invoked. You can still save an instance of the builder
     * and invoke this method again to receive a new instance of an
     * {@link EventHandler} based of the builder.
     *
     * @return {@link EventHandler}
     */
    @NotNull
    EventHandler<T> build();

    /**
     * Registers and {@link EventSubscription} based of the builder.
     * <br>
     * This is mainly a shortcut for {@link EventBuilder#build()} and
     * then {@link EventHandler#register(Plugin)}
     * <br>
     * Note when invoking this method, it will implicitly create an
     * {@link EventHandler} based of the builder with
     * {@link org.bukkit.event.EventPriority#NORMAL} and setting it
     * not ignore cancelled events. You can still save an instance of
     * the builder and invoke this method again to receive a new instance
     * of an {@link EventSubscription} based of the builder.
     *
     * @param plugin owner
     *
     * @return {@link EventSubscription}
     */
    @NotNull
    EventSubscription<T> register(@NotNull Plugin plugin);
}
