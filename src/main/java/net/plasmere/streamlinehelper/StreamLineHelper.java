package net.plasmere.streamlinehelper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.plasmere.streamlinehelper.config.ConfigHandler;
import net.plasmere.streamlinehelper.runnables.OneSecondRunnable;
import net.plasmere.streamlinehelper.utils.Messenger;
import net.plasmere.streamlinehelper.utils.PlayerUtils;
import net.plasmere.streamlinehelper.utils.StringUtils;
import net.plasmere.streamlinehelper.utils.holders.CMIHolder;
import net.plasmere.streamlinehelper.utils.holders.EssXHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public final class StreamLineHelper extends JavaPlugin implements PluginMessageListener {

    public final static String customChannel = "streamline:channel";
    public static StreamLineHelper instance;
    public static ConfigHandler config;
    public static CMIHolder cmiHolder;
    public static EssXHolder essXHolder;

    public BukkitRunnable secondsTimer;

    public PluginManager pm;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.pm = this.getServer().getPluginManager();

        config = new ConfigHandler();
        cmiHolder = new CMIHolder();
        essXHolder = new EssXHolder();

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
        PlayerUtils.removeAllPlayerFinders();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (! channel.equals(customChannel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        getLogger().info("Received plugin message from StreamLine Main on sub-channel \"" + subchannel + "\".");

        if (subchannel.equals("request.displayname")) {
            Messenger.sendDisplayNameUpdate(player);
            return;
        }
        if (subchannel.equals("teleport")) {
            PlayerUtils.addNewRepeatingPlayerFinder(in.readUTF(), in.readUTF());
        }
        if (subchannel.equals("tag-ping")) {
            PlayerUtils.pingPlayer(in.readUTF());
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
