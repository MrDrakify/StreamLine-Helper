package net.plasmere.streamlinehelper.listeners;

import net.plasmere.streamlinehelper.objects.getting.SavableGuild;
import net.plasmere.streamlinehelper.utils.Messenger;
import net.plasmere.streamlinehelper.utils.PluginUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Messenger.sendDisplayNameUpdate(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent event) {
        PluginUtils.pushAllGuilds();
        PluginUtils.pushAllParties();
    }
}
