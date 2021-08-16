package net.plasmere.streamlinehelper.utils;

import net.plasmere.streamlinehelper.StreamLineHelper;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    public static List<Player> onlinePlayers() {
        return new ArrayList<>(StreamLineHelper.instance.getServer().getOnlinePlayers());
    }

    public static void updateAllPlayers(){
        for (Player player : onlinePlayers()) {
            Messenger.sendDisplayNameUpdate(player);
        }
    }

    public static Player findPlayerByUUID(String uuid) {
        for (Player player : onlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) return player;
        }

        return null;
    }
}
