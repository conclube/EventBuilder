package me.conclure.eventbuilder.implementation;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.conclure.eventbuilder.interfaces.EventSubscription;
import me.conclure.eventbuilder.internal.PredicateConsumer;
import me.conclure.eventbuilder.internal.UnregisterPredicate;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
class EventSubscriptionImpl<T extends Event> implements EventSubscription<T>, EventExecutor, Listener {

    static final HandlerListCache HANDLER_LIST_CACHE = new HandlerListCache();

    final Class<T> eventType;
    final EventPriority eventPriority;
    final boolean ignoredCancelled;
    final Object[] handlerArray;
    final Consumer<Exception>[] exceptionArray;
    final AtomicBoolean isRegistered;
    final Plugin plugin;

    EventSubscriptionImpl(Plugin plugin,
                          EventHandlerImpl<T> eventHandler) {
        this.plugin = plugin;
        eventType = eventHandler.eventType;
        eventPriority = eventHandler.eventPriority;
        ignoredCancelled = eventHandler.ignoreCancelled;
        handlerArray = new Object[eventHandler.actionList.size()];
        exceptionArray = new Consumer[eventHandler.exceptionList.size()];
        isRegistered = new AtomicBoolean();
        for (int i = 0; i < handlerArray.length; i++) {
            handlerArray[i] = eventHandler.actionList.get(i);
        }
        for (int i = 0; i < exceptionArray.length; i++) {
            exceptionArray[i] = eventHandler.exceptionList.get(i);
        }
        register();
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
    public Plugin getOwner() {
        return plugin;
    }

    @Override
    public boolean unregister() {
        if (!isRegistered.get()) {
            return false;
        }
        HANDLER_LIST_CACHE.apply(eventType,this).unregister(this);
        return true;
    }

    @Override
    public boolean register() {
        if (isRegistered.get()) {
            return false;
        }
        register0();
        return true;
    }

    void register0() {
        Bukkit.getPluginManager().registerEvent(eventType,this,eventPriority,this,plugin,ignoredCancelled);
    }

    @Override
    public void execute(Listener listener,
                        Event event) throws EventException {
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
                if (o == null) {
                    continue;
                }

                if (o instanceof PredicateConsumer && ((PredicateConsumer<T>)o).getPredicate().test(eventCasted)) {
                    ((PredicateConsumer<T>)o).getConsumer().accept(eventCasted);
                }
                else if (o instanceof Predicate && !((Predicate<T>)o).test(eventCasted)) {
                    break;
                }
                else if (o instanceof Consumer) {
                    ((Consumer<T>)o).accept(eventCasted);
                }
                else if (o instanceof UnregisterPredicate && ((UnregisterPredicate<T>)o).getPredicate().test(eventCasted)) {
                    unregister();
                }
            }
        } catch (ClassCastException exc) {
            throw new EventException(exc);
        } catch (Exception exc) {
            if (exceptionArray.length == 0) {
                return;
            }
            for (Consumer<Exception> exceptionConsumer : exceptionArray) {
                exceptionConsumer.accept(exc);
            }
        }
    }

    static class HandlerListCache implements BiFunction<Class<? extends Event>,EventSubscriptionImpl<?>,HandlerList> {

        final Map<Class<? extends Event>,HandlerList> cacheMap = new HashMap<>();

        @Override
        public HandlerList apply(Class<? extends Event> aClass, EventSubscriptionImpl<?> eventSubscription) {
            HandlerList handlerList = cacheMap.get(aClass);
            if (handlerList == null) {
                try {
                    handlerList = cache(aClass);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                } finally {
                    cacheMap.put(aClass,handlerList);
                }
            }
            return handlerList;
        }

        HandlerList cache(Class<? extends Event> aClass) throws ReflectiveOperationException {
            Method method_getHandlerList = aClass.getMethod("getHandlerList");
            return (HandlerList) method_getHandlerList.invoke(null);
        }
    }
}
