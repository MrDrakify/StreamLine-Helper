package net.plasmere.streamlinehelper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.plasmere.streamlinehelper.runnables.OneSecondRunnable;
import net.plasmere.streamlinehelper.utils.Messenger;
import net.plasmere.streamlinehelper.utils.PlayerUtils;
import net.plasmere.streamlinehelper.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public final class StreamLineHelper extends JavaPlugin implements PluginMessageListener {

    public final static String customChannel = "streamline:channel";
    public static StreamLineHelper instance;

    public BukkitRunnable secondsTimer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        //checkIfBungee();

        getServer().getMessenger().registerIncomingPluginChannel(this, customChannel, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, customChannel);

        secondsTimer = new OneSecondRunnable();
        secondsTimer.runTaskTimerAsynchronously(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (! channel.equals(customChannel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("request.displayname")) {
            Messenger.sendDisplayNameUpdate(player);
            return;
        }
        if (subchannel.equals("teleport")) {
            Player to = PlayerUtils.findPlayerByUUID(in.readUTF());
            if (to == null) {
                player.sendMessage(StringUtils.codedString("&cSorry, but we could not find the player to teleport to!"));
                return;
            }

            player.teleport(to);
        }
    }

    // we check like that if the specified server is BungeeCord.
    private void checkIfBungee()
    {
        if (! getServer().spigot().getConfig().getBoolean("settings.bungeecord"))
        {
            getLogger().severe("This server is not BungeeCord.");
            getLogger().severe("If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell.");
            getLogger().severe("Plugin disabled!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
