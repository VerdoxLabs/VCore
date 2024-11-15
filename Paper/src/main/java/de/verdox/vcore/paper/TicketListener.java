package de.verdox.vcore.paper;

import de.verdox.vpipeline.api.NetworkParticipant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class TicketListener implements Listener {
    private final NetworkParticipant networkParticipant;
    public TicketListener(NetworkParticipant networkParticipant) {
        this.networkParticipant = networkParticipant;
    }

    @EventHandler
    public void preloadDataOnConnect(AsyncPlayerPreLoginEvent e){
        networkParticipant.messagingService().getTicketPropagator().triggerTicketDataPreloadGroup(PlayerConnectionTicket.class);
    }
    @EventHandler
    public void consumeTicketsOnJoin(PlayerJoinEvent e) {
        networkParticipant.messagingService().getTicketPropagator().consumeTicketGroup(PlayerConnectionTicket.class, e.getPlayer());
    }
}
