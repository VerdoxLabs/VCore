package de.verdox.vcore.paper;

import de.verdox.vpipeline.api.NetworkParticipant;
import io.papermc.paper.event.player.PlayerClientLoadedWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TicketListener implements Listener {
    private final JavaPlugin javaPlugin;
    private final NetworkParticipant networkParticipant;
    public TicketListener(JavaPlugin javaPlugin, NetworkParticipant networkParticipant) {
        this.javaPlugin = javaPlugin;
        this.networkParticipant = networkParticipant;
    }

    @EventHandler
    public void preloadDataOnConnect(AsyncPlayerPreLoginEvent e){
        networkParticipant.messagingService().getTicketPropagator().triggerTicketDataPreloadGroup(PlayerConnectionTicket.class);
    }
    @EventHandler
    public void consumeTicketsOnJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(javaPlugin, () -> {
            networkParticipant.messagingService().getTicketPropagator().consumeTicketGroup(PlayerConnectionTicket.class, e.getPlayer());
        });
    }
}
