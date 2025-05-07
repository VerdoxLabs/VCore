package de.verdox.vcore.paper;

import de.verdox.vpipeline.api.NetworkParticipant;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VCorePaper {
    public static void onPluginLoad(PluginClasspathBuilder pluginClasspathBuilder){
        VPipelineDependencyLoader.load(pluginClasspathBuilder);
    }
    public static void enableListeners(JavaPlugin javaPlugin, NetworkParticipant networkParticipant){
        TicketListener ticketListener = new TicketListener(javaPlugin, networkParticipant);
        Bukkit.getPluginManager().registerEvents(ticketListener, javaPlugin);
    }
}
