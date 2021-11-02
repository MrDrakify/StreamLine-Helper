package net.plasmere.streamlinehelper.utils.holder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.objects.placeholders.PlaceHolders;
import net.plasmere.streamlinehelper.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PAPIHolder {
    public boolean enabled;

    public PAPIHolder(){
        enabled = isPresent();
    }

    public boolean isPresent(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return false;
        }

        try {
            new PlaceHolders().register();
            return true;
        } catch (Exception e) {
            Messenger.logSevere("LuckPerms not loaded... Disabling LuckPerms support...");
        }
        return false;
    }
}
