package net.plasmere.streamlinehelper.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.plasmere.streamlinehelper.StreamLineHelper;
import org.bukkit.entity.Player;

public class Messenger {
    public static void sendDisplayNameUpdate(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("send.displayname");
        out.writeUTF(player.getDisplayName());

        player.sendPluginMessage(StreamLineHelper.instance, StreamLineHelper.customChannel, out.toByteArray());
    }
}
