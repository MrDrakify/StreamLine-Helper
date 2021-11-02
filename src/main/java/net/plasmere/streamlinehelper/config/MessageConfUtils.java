package net.plasmere.streamlinehelper.config;

import net.plasmere.streamlinehelper.StreamLineHelper;

public class MessageConfUtils {
    // Messages:
//    public static String s = StreamLineHelper.config.getMessString("");
    // Basics.
    public static String version() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("version");
    }
    public static String noPerm() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("no-permission");
    }
    public static String onlyPlayers() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("only-players");
    }
    public static String noPlayer() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("no-player");
    }
    // Command needs args.
    public static String needsMore() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("command-needs-args.more");
    }
    public static String needsLess() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("command-needs-args.less");
    }
    // Debug.
    public static String debugMessage() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("debug.message");
    }
    public static String debugToggleOn() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("debug.toggle.on");
    }
    public static String debugToggleOff() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("debug.toggle.off");
    }
    // Script.
    public static String scriptMessage() {
        StreamLineHelper.config.reloadMessages();
        return StreamLineHelper.config.getMessString("script.message");
    }
}