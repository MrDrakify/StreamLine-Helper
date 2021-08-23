package net.plasmere.streamlinehelper.config;

import net.plasmere.streamlinehelper.StreamLineHelper;

public class MessageConfUtils {
    // Messages:
//    public static String s = StreamLineHelper.config.getMessString("");
    // Basics.
    public static String version = StreamLineHelper.config.getMessString("version");
    public static String noPerm = StreamLineHelper.config.getMessString("no-permission");
    public static String onlyPlayers = StreamLineHelper.config.getMessString("only-players");
    public static String noPlayer = StreamLineHelper.config.getMessString("no-player");
    // Command needs args.
    public static String needsMore = StreamLineHelper.config.getMessString("command-needs-args.more");
    public static String needsLess = StreamLineHelper.config.getMessString("command-needs-args.less");
    // Debug.
    public static String debugMessage = StreamLineHelper.config.getMessString("debug.message");
    public static String debugToggleOn = StreamLineHelper.config.getMessString("debug.toggle.on");
    public static String debugToggleOff = StreamLineHelper.config.getMessString("debug.toggle.off");
}