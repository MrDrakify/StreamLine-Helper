package net.plasmere.streamlinehelper.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String codedString(String string) {
        return ChatColor.translateAlternateColorCodes('&', newLined(string));
    }

    public static String newLined(String string) {
        return string.replace("%newlined%", "\n");
    }

    public static String removeExtraDot(String string){
        String s = string.replace("..", ".");

        if (s.endsWith(".")) {
            s = s.substring(0, s.lastIndexOf('.'));
        }

        return s;
    }
}
