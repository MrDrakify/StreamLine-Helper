package net.plasmere.streamlinehelper.commands;

import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.config.MessageConfUtils;
import net.plasmere.streamlinehelper.utils.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            boolean debug = StreamLineHelper.config.getConfBoolean("debug");
            StreamLineHelper.config.setObjectConf("debug", ! debug);

            Messenger.sendMessage(sender, MessageConfUtils.debugMessage()
                    .replace("%toggle%", StreamLineHelper.config.getConfBoolean("debug") ? MessageConfUtils.debugToggleOn() : MessageConfUtils.debugToggleOff())
            );
        } else {
            Messenger.sendMessage(sender, MessageConfUtils.needsLess());
            return false;
        }
        return true;
    }
}
