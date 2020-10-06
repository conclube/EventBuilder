package me.conclure.eventbuilder.implementation;

import me.conclure.eventbuilder.interfaces.EventSubscription;
import me.conclure.eventbuilder.internal.HandlerException;
import me.conclure.eventbuilder.internal.PredicateConsumer;
import me.conclure.eventbuilder.internal.UnregisterPredicate;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
class EventSubscriptionImpl<T extends Event> implements EventSubscription<T>, EventExecutor, Listener {

    static final BiConsumer<Class<?>, EventSubscriptionImpl<?>> UNREGISTER_CONSUMER = (eventType,eventSubscription) -> {
        try {
            Method method_getHandlerList = eventType.getMethod("getHandlerList");
            HandlerList handlerList = (HandlerList)method_getHandlerList.invoke(null);
            handlerList.unregister(eventSubscription);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    };

    final Class<T> eventType;
    final EventPriority eventPriority;
    final boolean ignoredCancelled;
    final Object[] handlerArray;
    final Consumer<Exception>[] exceptionArray;
    final AtomicBoolean isRegistered;

    EventSubscriptionImpl(Plugin plugin,
                          EventHandlerImpl<T> eventHandler) {
        eventType = eventHandler.builder.eventType;
        eventPriority = eventHandler.builder.eventPriority;
        ignoredCancelled = eventHandler.builder.ignoreCancelled;
        handlerArray = new Object[eventHandler.builder.actionList.size()];
        exceptionArray = new Consumer[eventHandler.builder.exceptionList.size()];
        isRegistered = new AtomicBoolean();
        for (int i = 0; i < handlerArray.length; i++) {
            handlerArray[i] = eventHandler.builder.actionList.get(i);
        }
        for (int i = 0; i < exceptionArray.length; i++) {
            exceptionArray[i] = eventHandler.builder.exceptionList.get(i);
        }
        Bukkit.getPluginManager().registerEvent(eventType,this,eventPriority,this,plugin,ignoredCancelled);
    }

    @Override
    public boolean isRegistered() {
        return isRegistered.get();
    }

    @Override
    public EventPriority getPriority() {
        return eventPriority;
    }

    @Override
    public boolean ignoreCancelled() {
        return ignoredCancelled;
    }

    @Override
    public Class<T> getEventType() {
        return eventType;
    }

    @Override
    public boolean unregister() {
        if (!isRegistered.getAndSet(false)) {
            return false;
        }
        UNREGISTER_CONSUMER.accept(eventType,this);
        return true;
    }

    @Override
    public void execute(Listener listener,
                        Event event) {
        if (eventType != event.getClass()) {
            return;
        }

        if (!isRegistered.get()) {
            event.getHandlers().unregister(listener);
            return;
        }

        T eventCasted = eventType.cast(event);

        try {
            for (Object o : handlerArray) {
                if (o instanceof PredicateConsumer && ((PredicateConsumer<T>)o).getPredicate().test(eventCasted)) {
                    ((PredicateConsumer<T>)o).getConsumer().accept(eventCasted);
                }
                else if (o instanceof Consumer) {
                    ((Consumer<T>)o).accept(eventCasted);
                }
                else if (o instanceof Predicate && !((Predicate<T>)o).test(eventCasted)) {
                    break;
                }
                else if (o instanceof UnregisterPredicate && ((UnregisterPredicate<T>)o).getPredicate().test(eventCasted)) {
                    unregister();
                }
            }
        } catch (Exception exc) {
            for (Consumer<Exception> exceptionConsumer : exceptionArray) {
                exceptionConsumer.accept(exc);
            }
        }
    }
}
