package net.plasmere.streamlinehelper.objects.getting;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.plasmere.streamlinehelper.objects.getting.chats.ChatChannel;
import net.plasmere.streamlinehelper.utils.Messenger;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

public class SavablePlayer extends SavableUser {
    public int totalXP;
    public int currentXP;
    public int lvl;
    public int playSeconds;
    public String ips;
    public String names;
    public String latestIP;
    public List<String> ipList;
    public List<String> nameList;
    public boolean muted;
    public Date mutedTill;
    public Player player;
    public ChatChannel chatChannel;
    public String chatIdentifier;
    public long discordID;

    public int defaultLevel = 1;

    public SavablePlayer(String[] lines) {
        super(lines);
    }

    public boolean onlineCheck() {
        return this.online;
    }

    @Override
    public void loadMoreVars() {
        this.online = onlineCheck();
        if (!this.online) this.latestVersion = getFromKey("latest-version");

        this.ips = getFromKey("ips");
        this.names = getFromKey("names");
        this.latestIP = getFromKey("latest-ip");
        this.ipList = loadIPs();
        this.nameList = loadNames();
        this.playSeconds = Integer.parseInt(getFromKey("playtime"));
        this.muted = Boolean.parseBoolean(getFromKey("muted"));
        try {
            this.mutedTill = new Date(Long.parseLong(getFromKey("muted-till")));
        } catch (Exception e) {
            this.mutedTill = null;
        }

        this.lvl = Integer.parseInt(getFromKey("lvl"));
        this.totalXP = Integer.parseInt(getFromKey("total-xp"));
        this.currentXP = Integer.parseInt(getFromKey("current-xp"));

//        this.chatChannel = parseChatLevel(getFromKey("chat-channel"));
        this.chatIdentifier = getFromKey("chat-identifier");

        try {
            this.discordID = Long.parseLong(getFromKey("discord-id"));
        } catch (Exception e) {
            this.discordID = 0L;
        }
    }

    public String setChatIdentifier(String newIdentifier) {
        this.chatIdentifier = newIdentifier;
        updateKey("chat-identifier", this.chatIdentifier);

        return newIdentifier;
    }

    public void tryAddNewName(String name) {
        if (nameList == null) this.nameList = new ArrayList<>();

        if (nameList.contains(name)) return;

        this.nameList.add(name);

        this.names = stringifyList(nameList, ",");

        updateKey("names", this.names);
    }

    public void tryRemName(String name) {
        if (nameList == null) this.nameList = new ArrayList<>();

        if (!nameList.contains(name)) return;

        this.nameList.remove(name);

        this.names = stringifyList(nameList, ",");

        updateKey("names", this.names);
    }

    public void tryAddNewIP(String ip) {
        if (ipList == null) this.ipList = new ArrayList<>();

        if (ipList.contains(ip)) return;

        this.ipList.add(ip);

        this.ips = stringifyList(ipList, ",");

        updateKey("ips", this.ips);
    }

    public void tryRemIP(String ip) {
        if (ipList == null) this.ipList = new ArrayList<>();

        if (!ipList.contains(ip)) return;

        this.ipList.remove(ip);

        this.ips = stringifyList(ipList, ",");

        updateKey("ips", this.ips);
    }

    public void addPlaySecond(int amount) {
        setPlaySeconds(playSeconds + amount);
    }

    public void setPlaySeconds(int amount) {
        updateKey("playtime", amount);
    }

    public double getPlayDays() {
        return playSeconds / (60.0d * 60.0d * 24.0d);
    }

    public double getPlayHours() {
        return playSeconds / (60.0d * 60.0d);
    }

    public double getPlayMinutes() {
        return playSeconds / (60.0d);
    }

    public List<String> loadIPs() {
        List<String> thing = new ArrayList<>();

        String search = "ips";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (!getFromKey(search).contains(",")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
                if (t == null) continue;
                if (t.equals("")) continue;

                try {
                    thing.add(t);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadNames() {
        List<String> thing = new ArrayList<>();

        String search = "names";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (!getFromKey(search).contains(",")) {
                thing.add(getFromKey(search));
                return thing;
            }

            for (String t : getFromKey(search).split(",")) {
                if (t == null) continue;
                if (t.equals("")) continue;

                try {
                    thing.add(t);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */

    public int getNeededXp(int forLevel) {
        int needed = 0;

        needed = 2500 + (2500 * (forLevel - defaultLevel));

        return needed;
    }

    public int xpUntilNextLevel() {
        return getNeededXp(this.lvl + 1) - this.totalXP;
    }

    public void addTotalXP(int amount) {
        setTotalXP(amount + this.totalXP);
    }

    public void setTotalXP(int amount) {
        this.totalXP = amount;

        while (xpUntilNextLevel() <= 0) {
            int setLevel = this.lvl + 1;
            updateKey("lvl", setLevel);
        }

        updateKey("total-xp", amount);
        updateKey("current-xp", getCurrentXP());
    }

    public int getCurrentLevelXP() {
        int xpTill = 0;
        for (int i = 0; i <= this.lvl; i++) {
            xpTill += getNeededXp(i);
        }

        return xpTill;
    }

    public int getCurrentXP() {
        return this.totalXP - getCurrentLevelXP();
    }

    public void setMuted(boolean value) {
        muted = value;
        updateKey("muted", value);
    }

    public void setMutedTill(long value) {
        mutedTill = new Date(value);
        updateKey("muted-till", value);
    }

    public void removeMutedTill() {
        mutedTill = null;
        updateKey("muted-till", "");
    }

    public void updateMute(boolean set, Date newMutedUntil) {
        setMuted(set);
        setMutedTill(newMutedUntil.getTime());
    }

    public void toggleMuted() {
        setMuted(!muted);
    }

    public void setDiscordID(long id) {
        this.discordID = id;
        updateKey("discord-id", id);
    }
}