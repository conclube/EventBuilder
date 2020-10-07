# EventBuilder
Functional event listener builder for bukkit events.

## Setup
To use this, you will have to shade it into your jar.

```
http://repo.bristermitten.me/
me.conclure:event-builder:1.0.0
```

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
You may now set the event priority by `EventHandler<T extends Event>#eventPriority(EventPriority)` 
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
EventSubscription<PlayerJoinEvent> eventSubscription = eventHandler.register(myPluginInstance);
```

<br>

If you prefer, you may also chain all the methods as a builder pattern.

```java
EventSubscription<PlayerJoinEvent> eventSubscription = EventBuilders.create(PlayerJoinEvent.class)
  .execute(event -> event.setJoinMessage("Someone joined."))
  .build()
  .ignoreCancelled(true)
  .eventPriority(EventPriority.MONITOR)
  .register(myPluginInstance);
```

**NOTE**: You can skip calling `#build()` and instead call `#register(Plugin)` directly, 
that will set ignoreCancelled to false and eventPriority to NORMAL.

<br>

It's recommended to unregister the subscription when your plugin is disabling `JavaPlugin#onDisable()`.

```java

@Override
public void onDisable() {
  eventSubscription.unregister();
}

```

### Advanced
Now let's look into more functionalites you can utilize.
Firstly you can use a `Predicate<T extends Event>` as a filter.
This will cause all actions declared under the filter to not execute if the filter doesn't return true.

```java
EventBuilder<PlayerJoinEvent> eventBuilder = EventBuilders.create(PlayerJoinEvent.class)
  .filter(event -> !event.isAsynchronous())
  .execute(event -> event.setJoinMessage("Someone joined.")); //Will only run if the event isn't async
```

<br>

There is also `EventBuilder<T extend Event>#unregisterIf(Predicate<T>)` which instead of filtering will unregister the event subscription if the predicate is true.

```java
eventBuilder.unregisterIf(Event::isAsynchronous);
```

**NOTE**: Any actions declared underneath it will still run that time.

<br>

You can also utilize `EventBuilder<T extend Event>#executeIf(Predicate<T>,Consumer<T>)`.
The consumer will only run if the predicate is true. The predicate won't apply to any other actions else than the consumer declared after it 
albeit filters above it will still apply.

```java
//Will only run if the event is async
eventBuilder.executeIf(event -> event.isAsynchronous(), event.setJoinMessage("Nobody joined."));
```

```java
eventBuilder.filter(event -> event.getPlayer().hasPlayerBefore())

  //Will only run if the event is async and if the player has played before
  .executeIf(event -> event.isAsynchronous(), event.setJoinMessage("Nobody joined."));
```

```java
eventBuilder.filter(event -> event.getPlayer().hasPlayedBefore())

  //Will only run if the event is async and if the player has played before
  .executeIf(event -> event.isAsynchronous(), event.setJoinMessage("Nobody joined."))

  //Will only run if the player has played before
  .execute(event -> event.setJoinMessage("An OG player joined."));
```

## Contributions
This project is open for any pull requests that has reasonable changes. 
