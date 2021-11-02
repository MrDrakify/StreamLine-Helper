package net.plasmere.streamlinehelper.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.config.MessageConfUtils;
import net.plasmere.streamlinehelper.utils.Messenger;
import net.plasmere.streamlinehelper.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SendScriptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            Messenger.sendMessage(sender, MessageConfUtils.needsMore());
            return false;
        } else if (args.length > 2) {
            Messenger.sendMessage(sender, MessageConfUtils.needsLess());
            return false;
        } else {
            Player player = PlayerUtils.findPlayerByName(args[0]);

            if (player == null) {
                Messenger.sendMessage(sender, MessageConfUtils.noPlayer());

                List<String> users = StreamLineHelper.config.getUsersStringList("to-vote");
                if (! users.contains(args[0])) users.add(args[0]);

                StreamLineHelper.config.setUsersStringList("to-vote", users);
                return false;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("run.script");
            out.writeUTF(player.getName());
            out.writeUTF(args[1]);

            player.sendPluginMessage(StreamLineHelper.instance, StreamLineHelper.customChannel, out.toByteArray());

            Messenger.sendMessage(sender, MessageConfUtils.scriptMessage()
                    .replace("%player%", player.getDisplayName())
            );
        }
        return true;
    }
}
