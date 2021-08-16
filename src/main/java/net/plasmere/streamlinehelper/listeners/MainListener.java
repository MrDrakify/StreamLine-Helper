package net.plasmere.streamlinehelper.listeners;

import net.plasmere.streamlinehelper.utils.Messenger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MainListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Messenger.sendDisplayNameUpdate(event.getPlayer());
    }
}
