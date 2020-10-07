# EventBuilder
Functional event listener builder for bukkit events.

## How to use
You will start everything with the class `EventBuilders`.
Then call `EventBuilders.create(Class<T extends Event>)` which will give you an `EventBuilder<T extends Event>`.
```java
EventBuilder<PlayerJoinEvent> eventBuilder = EventBuilders.create(PlayerJoinEvent.class);
```
**NOTE**: You may not use event classes which doesn't have the static method `#getHandlerList()`.
