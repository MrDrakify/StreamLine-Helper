package net.plasmere.streamlinehelper.objects.getting;

import net.md_5.bungee.api.chat.BaseComponent;
import net.plasmere.streamlinehelper.config.ConfigUtils;
import net.plasmere.streamlinehelper.utils.Messenger;
import net.plasmere.streamlinehelper.utils.PlayerUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class SavableUser {
    private TreeMap<String, String> info = new TreeMap<>();
    private SavableUser savableUser;

    public String uuid;
    public String latestName;
    public String displayName;
    public String guild;
    public String party;
    public String tags;
    public List<String> tagList;
    public int points;
    public String lastFromUUID;
    public String lastToUUID;
    public String replyToUUID;
    public String lastMessage;
    public String lastToMessage;
    public String lastFromMessage;
    public String ignoreds;
    public List<String> ignoredList;
    public String friends;
    public List<String> friendList;
    public String pendingToFriends;
    public List<String> pendingToFriendList;
    public String pendingFromFriends;
    public List<String> pendingFromFriendList;
    public String latestVersion;
//    public String latestServer;
    public boolean online;
    public boolean sspy;
    public boolean gspy;
    public boolean pspy;
    public boolean viewsc;
    public boolean sc;
    public boolean sspyvs;
    public boolean pspyvs;
    public boolean gspyvs;
    public boolean scvs;

    public List<String> savedKeys = new ArrayList<>();

    public SavableUser(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("#")) continue;
            if (! lines[i].contains("=")) continue;
            String[] spit = lines[i].split("=", 2);
            if (spit.length != 2) continue;
            addKeyValuePair(spit[0], spit[1]);
        }

        loadVars();
    }

    public TreeMap<String, String> getInfo() {
        return info;
    }

    public void remKey(String key) {
        info.remove(key);
    }

    public String getFromKey(String key) {
        return info.get(key);
    }

    public void addKeyValuePair(String key, String value) {
        if (info.containsKey(key)) return;

        info.put(key, value);
    }

    public void updateKey(String key, Object value) {
        info.remove(key);
        addKeyValuePair(key, String.valueOf(value));
        loadVars();
    }

    public String stringifyList(List<String> list, String splitter){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i <= list.size(); i++) {
            if (i < list.size()) {
                stringBuilder.append(list.get(i - 1)).append(splitter);
            } else {
                stringBuilder.append(list.get(i - 1));
            }
        }

        return stringBuilder.toString();
    }

    public void loadVars() {
        this.uuid = getFromKey("uuid");
        this.latestName = getFromKey("latest-name");
        this.displayName = getFromKey("display-name");
        this.guild = getFromKey("guild");
        this.party = getFromKey("party");
        this.tagList = loadTags();
        this.points = Integer.parseInt(getFromKey("points") == null ? "0" : getFromKey("points"));
        this.lastFromUUID = getFromKey("last-from");
        this.lastToUUID = getFromKey("last-to");
        this.lastMessage = getFromKey("last-message");
        this.lastToMessage = getFromKey("last-to-message");
        this.lastFromMessage = getFromKey("last-from-message");
        this.replyToUUID = getFromKey("reply-to");
        this.ignoredList = loadIgnored();
        this.friendList = loadFriends();
        this.pendingToFriendList = loadPendingToFriends();
        this.pendingFromFriendList = loadPendingFromFriends();
        this.sspy = Boolean.parseBoolean(getFromKey("sspy"));
        this.gspy = Boolean.parseBoolean(getFromKey("gspy"));
        this.pspy = Boolean.parseBoolean(getFromKey("pspy"));
        this.sc = Boolean.parseBoolean(getFromKey("sc"));
        this.sspyvs = Boolean.parseBoolean(getFromKey("sspy-vs"));
        this.pspyvs = Boolean.parseBoolean(getFromKey("pspy-vs"));
        this.gspyvs = Boolean.parseBoolean(getFromKey("gspy-vs"));
        this.scvs = Boolean.parseBoolean(getFromKey("sc-vs"));
        this.viewsc = Boolean.parseBoolean(getFromKey("view-sc"));
//        this.latestServer = getFromKey("latest-server");

        loadMoreVars();
    }

    abstract public void loadMoreVars();

    public void tryAddNewTag(String tag){
        if (tagList == null) this.tagList = new ArrayList<>();

        if (tagList.contains(tag)) return;

        this.tagList.add(tag);

        this.tags = stringifyList(tagList, ",");

        updateKey("tags", this.tags);
    }

    public void tryRemTag(String tag){
        if (tagList == null) this.tagList = new ArrayList<>();

        if (! tagList.contains(tag)) return;

        this.tagList.remove(tag);

        this.tags = stringifyList(tagList, ",");

        updateKey("tags", this.tags);
    }

    public void tryAddNewIgnored(String uuid){
        if (ignoredList == null) this.ignoredList = new ArrayList<>();

        if (ignoredList.contains(uuid)) return;

        this.ignoredList.add(uuid);

        this.ignoreds = stringifyList(ignoredList, ",");

        updateKey("ignored", this.ignoreds);
    }

    public void tryRemIgnored(String uuid){
        if (ignoredList == null) this.ignoredList = new ArrayList<>();

        if (! ignoredList.contains(uuid)) return;

        this.ignoredList.remove(uuid);

        this.ignoreds = stringifyList(ignoredList, ",");

        updateKey("ignored", this.ignoreds);
    }

    public void tryAddNewFriend(String uuid){
        if (friendList == null) this.friendList = new ArrayList<>();

        tryRemPendingToFriend(uuid);
        tryRemPendingFromFriend(uuid);

        if (friendList.contains(uuid)) return;

        this.friendList.add(uuid);

        this.friends = stringifyList(friendList, ",");

        updateKey("friends", this.friends);
    }

    public void tryRemFriend(String uuid){
        if (friendList == null) this.friendList = new ArrayList<>();

        if (! friendList.contains(uuid)) return;

        this.friendList.remove(uuid);

        this.friends = stringifyList(friendList, ",");

        updateKey("friends", this.friends);
    }

    public void tryAddNewPendingToFriend(String uuid){
        if (pendingToFriendList == null) this.pendingToFriendList = new ArrayList<>();

        if (pendingToFriendList.contains(uuid)) return;

        this.pendingToFriendList.add(uuid);

        this.pendingToFriends = stringifyList(pendingToFriendList, ",");

        updateKey("pending-to-friends", this.pendingToFriends);
    }

    public void tryRemPendingToFriend(String uuid){
        if (pendingToFriendList == null) this.pendingToFriendList = new ArrayList<>();

        if (! pendingToFriendList.contains(uuid)) return;

        this.pendingToFriendList.remove(uuid);

        this.pendingToFriends = stringifyList(pendingToFriendList, ",");

        updateKey("pending-to-friends", this.pendingToFriends);
    }

    public void tryAddNewPendingFromFriend(String uuid){
        if (pendingFromFriendList == null) this.pendingFromFriendList = new ArrayList<>();

        if (pendingFromFriendList.contains(uuid)) return;

        this.pendingFromFriendList.add(uuid);

        this.pendingFromFriends = stringifyList(pendingFromFriendList, ",");

        updateKey("pending-from-friends", this.pendingFromFriends);
    }

    public void tryRemPendingFromFriend(String uuid){
        if (pendingFromFriendList == null) this.pendingFromFriendList = new ArrayList<>();

        if (! pendingFromFriendList.contains(uuid)) return;

        this.pendingFromFriendList.remove(uuid);

        this.pendingFromFriends = stringifyList(pendingFromFriendList, ",");

        updateKey("pending-from-friends", this.pendingFromFriends);
    }

    public List<String> loadTags(){
        List<String> thing = new ArrayList<>();

        String search = "tags";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadIgnored(){
        List<String> thing = new ArrayList<>();

        String search = "ignored";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadFriends(){
        List<String> thing = new ArrayList<>();

        String search = "friends";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadPendingToFriends(){
        List<String> thing = new ArrayList<>();

        String search = "pending-to-friends";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public List<String> loadPendingFromFriends(){
        List<String> thing = new ArrayList<>();

        String search = "pending-from-friends";

        try {
            if (getFromKey(search) == null) return thing;
            if (getFromKey(search).equals("")) return thing;

            if (! getFromKey(search).contains(",")) {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        return thing;
    }

    public void dispose() throws Throwable {
        try {
            PlayerUtils.removeStat(this);
            this.uuid = null;
        } finally {
            super.finalize();
        }
    }

    public void setPoints(int amount) {
        points = amount;
        updateKey("points", amount);
    }

    public void addPoints(int amount) {
        setPoints(points + amount);
    }

    public void remPoints(int amount) {
        setPoints(points - amount);
    }

    public void setLatestServer(String server) {
        //this.latestServer = server;
        updateKey("latest-server", server);
    }

    public void setSSPY(boolean value) {
        sspy = value;
        updateKey("sspy", value);
    }

    public void toggleSSPY() { setSSPY(! sspy); }

    public void setGSPY(boolean value) {
        gspy = value;
        updateKey("gspy", value);
    }

    public void toggleGSPY() { setGSPY(! gspy); }

    public void setPSPY(boolean value) {
        pspy = value;
        updateKey("pspy", value);
    }

    public void togglePSPY() { setPSPY(! pspy); }

    public void setSC(boolean value) {
        sc = value;
        updateKey("sc", value);
    }

    public void toggleSC() { setSC(! sc); }

    public void setSCView(boolean value) {
        viewsc = value;
        updateKey("view-sc", value);
    }

    public void toggleSCView() { setSCView(! viewsc); }

    public void setSSPYVS(boolean value) {
        sspyvs = value;
        updateKey("sspy-vs", value);
    }

    public void toggleSSPYVS() { setSSPYVS(! sspyvs); }

    public void setPSPYVS(boolean value) {
        pspyvs = value;
        updateKey("pspy-vs", value);
    }

    public void togglePSPYVS() { setPSPYVS(! pspyvs); }

    public void setGSPYVS(boolean value) {
        gspyvs = value;
        updateKey("gspy-vs", value);
    }

    public void toggleGSPYVS() { setGSPYVS(! gspyvs); }

    public void setSCVS(boolean value) {
        scvs = value;
        updateKey("sc-vs", value);
    }

    public void toggleSCVS() { setSCVS(! scvs); }

    public String toString(){
        return latestName;
    }

    public void updateLastMessage(String message){
        updateKey("last-message", message);
    }

    public void updateLastToMessage(String message){
        updateKey("last-to-message", message);
    }

    public void updateLastFromMessage(String message){
        updateKey("last-from-message", message);
    }

    public void updateLastFrom(SavableUser messenger){
        updateKey("last-from", messenger.uuid);
    }

    public void updateLastTo(SavableUser to){
        updateKey("last-to", to.uuid);
    }

    public void updateReplyTo(SavableUser to){
        updateKey("reply-to", to.uuid);
    }
    
    public String getName() {
        return latestName;
    }
}
