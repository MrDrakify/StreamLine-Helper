package net.plasmere.streamlinehelper.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String codedString(String string) {
        return ChatColor.translateAlternateColorCodes('&', newLined(string));
    }

    public static String newLined(String string) {
        return string.replace("%newlined%", "\n");
    }
}
