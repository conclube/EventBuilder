import me.conclure.eventbuilder.EventBuilders;
import me.conclure.eventbuilder.interfaces.EventSubscription;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    private EventSubscription<PlayerJoinEvent> eventSubscription;

    @Override
    public void onEnable() {

        eventSubscription = EventBuilders.create(PlayerJoinEvent.class)
                .eventPriority(EventPriority.MONITOR)
                .execute(event -> event.setJoinMessage("lol"))
                .unregisterIf(event -> !event.getPlayer().hasPlayedBefore())
                .filter(event -> event.getPlayer().isSneaking())
                .execute(event -> event.setJoinMessage("not lol"))
                .executeIf(event -> event.getPlayer().hasPermission("lol"),
                        event -> event.setJoinMessage("very lol"))
                .onError(Exception::printStackTrace)
                .build()
                .ignoreCancelled(true)
                .register(this);

    }

    @Override
    public void onDisable() {
        eventSubscription.unregister();
    }
}
