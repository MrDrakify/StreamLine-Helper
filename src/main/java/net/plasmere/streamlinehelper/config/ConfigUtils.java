package net.plasmere.streamlinehelper.config;

import net.plasmere.streamlinehelper.StreamLineHelper;

import java.util.List;

public class ConfigUtils {
    // ConfigHandler //
//    public static String s = StreamLineHelper.config.getConfString("");
    // Important.
    public static String version() {
        StreamLineHelper.config.reloadConfig();
        return StreamLineHelper.config.getConfString("version");
    }
    // Debug.
    public static boolean debug() {
        StreamLineHelper.config.reloadConfig();
        return StreamLineHelper.config.getConfBoolean("debug");
    }
    // Essentials API settings.
    public static String essUseOrder() {
        StreamLineHelper.config.reloadConfig();
        return StreamLineHelper.config.getConfString("essentials.use-order");
    }
    public static boolean essUseAPI() {
        StreamLineHelper.config.reloadConfig();
        return StreamLineHelper.config.getConfBoolean("essentials.get-api-display-names");
    }
}
