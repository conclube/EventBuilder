# EventBuilder
Functional event listener builder for bukkit events.

## Setup
Soon

## Usage

### Get Started
You will start everything with the class `EventBuilders`.
Then call `EventBuilders.create(Class<T extends Event>)` which will give you an `EventBuilder<T extends Event>`.
```java
EventBuilder<PlayerJoinEvent> eventBuilder = EventBuilders.create(PlayerJoinEvent.class);
```
**NOTE**: You may not use event classes which doesn't have the static method `#getHandlerList()`.
<br>
To assign an action to the builder, simply call `EventBuilder<T extends Event>#execute(Consumer<T>)`.
```java
eventBuilder.execute(event -> event.setJoinMessage("Someone joined."));
```
<br>
Then we can get the an `EventHandler<T extends Event>` by calling `EventBuilder<T extends Event>#build()` that won't allow any modifications to it's actions.
You may also set the event priority by `EventHandler<T extends Event>#eventPriority(EventPriority)` 
and if handler should ignore cancelled by `EventHandler<T extends Event>#ignoreCancelled(boolean)`.
```java
EventHandler<PlayerJoinEvent> eventHandler = eventBuilder.build()
  .ignoreCancelled(true)
  .eventPriority(EventPriority.MONITOR);
```
<br>
You can then register the event handler by calling `EventHandler<T extends Event>#register(Plugin)` which will give you an `EventSubscription<T extends Event>`.
Once you have gotten the subscription you won't be able to modify anything. The plugin instance should be your own.
```java
EventSubscription<PlayerJoinEvent> eventSubscription = eventHandler.register(myPluginInstance);`
```

## Contributions
This project is open for any pull requests that has reasonable changes. 
