package net.plasmere.streamlinehelper.utils;

import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.commands.DebugCommand;
import net.plasmere.streamlinehelper.commands.SendScriptCommand;
import net.plasmere.streamlinehelper.objects.getting.SavableGuild;
import net.plasmere.streamlinehelper.objects.getting.SavableParty;
import net.plasmere.streamlinehelper.objects.getting.SavablePlayer;
import net.plasmere.streamlinehelper.objects.getting.SavableUser;

import java.util.ArrayList;
import java.util.List;

public class PluginUtils {
    public static List<SavableGuild> guilds = new ArrayList<>();
    public static List<SavableParty> parties = new ArrayList<>();

    public static void registerCommands() {
        StreamLineHelper.instance.getCommand("streamdebug").setExecutor(new DebugCommand());
        StreamLineHelper.instance.getCommand("streamscript").setExecutor(new SendScriptCommand());
    }

    public static void registerEvents(){
//        StreamLineHelper.instance.pm.registerEvents(new MainListener(), StreamLineHelper.instance);
    }

    public static boolean containsGuild(SavableGuild guild) {
        for (SavableGuild g : guilds) {
            if (g.leaderUUID.equals(guild.leaderUUID)) return true;
        }

        return false;
    }

    public static SavableGuild addGuild(SavableGuild guild) {
        if (containsGuild(guild)) return getGuild(guild.leaderUUID);

        guilds.add(guild);

        return guild;
    }

    public static boolean containsParty(SavableParty party) {
        for (SavableParty p : parties) {
            if (p.leaderUUID.equals(party.leaderUUID)) return true;
        }

        return false;
    }

    public static SavableParty addParty(SavableParty party) {
        if (containsParty(party)) return getParty(party.leaderUUID);

        parties.add(party);

        return party;
    }

    public static SavableGuild getGuild(String thing) {
        thing = UUIDUtils.swapToName(thing);

        for (SavableGuild guild : guilds) {
            for (SavableUser user : guild.totalMembers) {
                if (user.latestName.equals(thing)) return guild;
            }
        }

        return null;
    }

    public static SavableParty getParty(String thing) {
        thing = UUIDUtils.swapToName(thing);

        for (SavableParty party : parties) {
            for (SavableUser user : party.totalMembers) {
                if (user.latestName.equals(thing)) return party;
            }
        }

        return null;
    }

    public static void pushAllGuilds() {
        for (SavableGuild guild : new ArrayList<>(guilds)) {
            guild.pushInfo();
            guilds.remove(guild);
        }
    }

    public static void pushAllParties() {
        for (SavableParty party : new ArrayList<>(parties)) {
            party.pushInfo();
            parties.remove(party);
        }
    }
}
