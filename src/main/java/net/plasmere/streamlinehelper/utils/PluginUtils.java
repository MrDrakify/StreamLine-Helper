package net.plasmere.streamlinehelper.utils;

import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.commands.DebugCommand;

public class PluginUtils {
    public static void registerCommands() {
        StreamLineHelper.instance.getCommand("streamdebug").setExecutor(new DebugCommand());
    }

    public static void registerEvents(){
//        StreamLineHelper.instance.pm.registerEvents(new MainListener(), StreamLineHelper.instance);
    }
}
