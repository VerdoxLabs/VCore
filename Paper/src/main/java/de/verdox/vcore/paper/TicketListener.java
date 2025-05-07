package de.verdox.vcore.paper;

import de.verdox.vpipeline.api.NetworkParticipant;
import io.papermc.paper.event.player.PlayerClientLoadedWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TicketListener implements Listener {
    private final JavaPlugin javaPlugin;
    private final NetworkParticipant networkParticipant;

    private final Set<UUID> potential = ConcurrentHashMap.newKeySet();

    public TicketListener(JavaPlugin javaPlugin, NetworkParticipant networkParticipant) {
        this.javaPlugin = javaPlugin;
        this.networkParticipant = networkParticipant;
    }

    @EventHandler
    public void preloadDataOnConnect(AsyncPlayerPreLoginEvent e) {
        networkParticipant.messagingService().getTicketPropagator().triggerTicketDataPreloadGroup(PlayerConnectionTicket.class);
        potential.add(e.getUniqueId());
    }

    @EventHandler
    public void consumeTicketsOnJoin(PlayerClientLoadedWorldEvent e) {
        if(!potential.contains(e.getPlayer().getUniqueId())) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(javaPlugin, () -> {
            networkParticipant.messagingService().getTicketPropagator().consumeTicketGroup(PlayerConnectionTicket.class, e.getPlayer());
        }, 20L);
        potential.remove(e.getPlayer().getUniqueId());
    }
}
