package net.plasmere.streamlinehelper.objects.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.objects.getting.SavableGuild;
import net.plasmere.streamlinehelper.objects.getting.SavableParty;
import net.plasmere.streamlinehelper.objects.getting.SavableUser;
import net.plasmere.streamlinehelper.utils.PlayerUtils;
import net.plasmere.streamlinehelper.utils.PluginUtils;
import net.plasmere.streamlinehelper.utils.UUIDUtils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PlaceHolders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "Nitrate";
    }

    @Override
    public @NotNull String getAuthor() {
        return "streamline";
    }

    @Override
    public @NotNull String getVersion() {
        return StreamLineHelper.instance.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] args = params.split("_");
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "guild":
                if (args.length < 3) return "";
                SavableGuild guild = PluginUtils.getGuild(args[1]);

                if (guild == null) return "";

                switch (args[2].toLowerCase(Locale.ROOT)) {
                    case "member":
                        if (args.length < 5) return "";

                        int at = 0;
                        try {
                            at = Integer.parseInt(args[3]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "";
                        }

                        SavableUser user = guild.getMemberAt(at);

                        switch (args[4].toLowerCase(Locale.ROOT)) {
                            default:
                            case "absolute":
                                return user.latestName;
                            case "formatted":
                                return user.displayName;
                        }
                    case "uuid":
                        return guild.leaderUUID;
                    case "name":
                        return guild.name;
                    case "level":
                        return String.valueOf(guild.lvl);
                    case "xp":
                        if (args.length < 4) return "";
                        if (args[3].equalsIgnoreCase("current")) return String.valueOf(guild.currentXP);
                        else return String.valueOf(guild.totalXP);
                }
                break;
            case "party":
                if (args.length < 3) return "";
                SavableParty party = PluginUtils.getParty(args[1]);

                if (party == null) return "";

                switch (args[2].toLowerCase(Locale.ROOT)) {
                    case "member":
                        if (args.length < 5) return "";

                        int at = 0;
                        try {
                            at = Integer.parseInt(args[3]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "";
                        }

                        SavableUser user = party.getMemberAt(at);

                        switch (args[4].toLowerCase(Locale.ROOT)) {
                            default:
                            case "absolute":
                                return user.latestName;
                            case "formatted":
                                return user.displayName;
                        }
                    case "uuid":
                        return party.leaderUUID;
                }
                break;
            case "player":
                if (args.length < 3) return "";

                SavableUser user = PlayerUtils.getStatByUUID(UUIDUtils.swapToUUID(args[1]));
                if (user == null) return "";

                switch (args[2].toLowerCase(Locale.ROOT)) {
                    default:
                    case "absolute":
                        return user.latestName;
                    case "formatted":
                        return user.displayName;
                }
            default:
                return "";
        }

        return "";
    }
}
