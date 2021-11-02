package net.plasmere.streamlinehelper.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.config.ConfigUtils;
import net.plasmere.streamlinehelper.objects.getting.SavableUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Messenger {
    public static void logInfo(String msg){
        if (msg == null) msg = "";
        StreamLineHelper.instance.getLogger().info(StringUtils.newLined(msg));
    }

    public static void logWarning(String msg){
        if (msg == null) msg = "";
        StreamLineHelper.instance.getLogger().warning(StringUtils.newLined(msg));
    }

    public static void logSevere(String msg){
        if (msg == null) msg = "";
        StreamLineHelper.instance.getLogger().severe(StringUtils.newLined(msg));
    }
    
    public static void sendDisplayNameUpdate(Player player){
        String toSend = player.getDisplayName();

        try {
            if (ConfigUtils.essUseAPI()) {
                String[] split = ConfigUtils.essUseOrder().split(",");
                if (split[0].equals("cmi") && StreamLineHelper.cmiHolder.isPresent()) {
                    toSend = StreamLineHelper.cmiHolder.api.getPlayerManager().getUser(player.getUniqueId()).getDisplayName();
                } else if (split[1].equals("essentials") && StreamLineHelper.essXHolder.enabled) {
                    toSend = StreamLineHelper.essXHolder.api.getUser(player.getUniqueId()).getDisplayName();
                }

                if (split[0].equals("essentials") && StreamLineHelper.essXHolder.enabled) {
                    toSend = StreamLineHelper.essXHolder.api.getUser(player.getUniqueId()).getDisplayName();
                } else if (split[1].equals("cmi") && StreamLineHelper.cmiHolder.enabled) {
                    toSend = StreamLineHelper.cmiHolder.api.getPlayerManager().getUser(player.getUniqueId()).getDisplayName();
                }
            } else {
                toSend = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("send.displayname");
        out.writeUTF(toSend);

        player.sendPluginMessage(StreamLineHelper.instance, StreamLineHelper.customChannel, out.toByteArray());

        if (ConfigUtils.debug()) StreamLineHelper.instance.getLogger().info("Sent getDisplayName for " + player.getName() + " --> " + toSend);
    }

    public static void sendGuildPluginMessageRequest(Player to, List<String> guildStuff) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("spigot.send.guild"); // the channel could be whatever you want

        try {
            for (String stuff : guildStuff) {
                if (stuff.startsWith("#")) continue;
                if (stuff.equals("")) continue;

                out.writeUTF(stuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        to.sendPluginMessage(StreamLineHelper.instance, StreamLineHelper.customChannel, out.toByteArray());
    }

    public static void sendPartyPluginMessageRequest(Player to, List<String> partyStuff) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("spigot.send.guild"); // the channel could be whatever you want

        try {
            for (String stuff : partyStuff) {
                if (stuff.startsWith("#")) continue;
                if (stuff.equals("")) continue;

                out.writeUTF(stuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        to.sendPluginMessage(StreamLineHelper.instance, StreamLineHelper.customChannel, out.toByteArray());
    }

    public static void sendSavableUserPluginMessageRequest(Player to, List<String> userStuff, String type) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("spigot.send.user"); // the channel could be whatever you want
        out.writeUTF(type);

        try {
            for (String stuff : userStuff) {
                if (stuff.startsWith("#")) continue;
                if (stuff.equals("")) continue;

                out.writeUTF(stuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        to.sendPluginMessage(StreamLineHelper.instance, StreamLineHelper.customChannel, out.toByteArray());
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(StringUtils.codedString(message));
    }
}
