package net.plasmere.streamlinehelper.objects.getting;

import net.plasmere.streamlinehelper.utils.Messenger;
import net.plasmere.streamlinehelper.utils.PlayerUtils;
import net.plasmere.streamlinehelper.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SavableParty {
    private TreeMap<String, String> info = new TreeMap<>();

    public List<String> savedKeys;

    public int maxSize;
    public File file;
    public SavableUser leader;
    public String leaderUUID;
    public List<SavableUser> moderators = new ArrayList<>();
    public List<String> modsByUUID= new ArrayList<>();
    public List<SavableUser> members = new ArrayList<>();
    public List<String> membersByUUID = new ArrayList<>();
    public List<SavableUser> totalMembers = new ArrayList<>();
    public List<String> totalMembersByUUID = new ArrayList<>();
    public List<SavableUser> invites = new ArrayList<>();
    public List<String> invitesByUUID = new ArrayList<>();
    public boolean isMuted;
    public boolean isPublic;

    public enum Level {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public SavableParty(String[] lines) {
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

    public File getFile() {
        return file;
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

    public void loadVars(){
        if (getFromKey("leader") == null) return;
        if (getFromKey("leader").equals("")) return;

        try {
            this.leaderUUID = getFromKey("leader");
        } catch (Exception e) {
            return;
        }

        this.leader = PlayerUtils.getStatByUUID(leaderUUID);

        if (this.leaderUUID == null) {
//            try {
//                throw new Exception("Improper use of the SavableParty's class! Report this to the owner of the StreamLine plugin...");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return;
        }
        if (this.leaderUUID.equals("null") || this.leaderUUID.equals("")) {
//            try {
//                throw new Exception("Improper use of the SavableParty's class! Report this to the owner of the StreamLine plugin...");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return;
        }

        this.modsByUUID = loadModerators();
        this.membersByUUID = loadMembers();
        this.totalMembersByUUID = loadTotalMembers();
        this.invitesByUUID = loadInvites();
        this.isMuted = Boolean.parseBoolean(getFromKey("muted"));
        this.isPublic = Boolean.parseBoolean(getFromKey("public"));
        this.maxSize = Integer.parseInt(getFromKey("max-size"));

        try {
            loadAllMembers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SavableUser getMember(String uuid) {
        return PlayerUtils.getStatByUUID(uuid);
    }

    public void removeUUID(String uuid) {
        updateKey("total-members", StringUtils.removeExtraDot(getFromKey("total-members").replace(uuid, "")));
        updateKey("members", StringUtils.removeExtraDot(getFromKey("members").replace(uuid, "")));
        updateKey("mods", StringUtils.removeExtraDot(getFromKey("mods").replace(uuid, "")));
        updateKey("uuid", StringUtils.removeExtraDot(getFromKey("uuid").replace(uuid, "")));
    }

    public void loadAllMembers(){
        loadMods();
        loadMems();
        loadTMems();
        loadInvs();
    }

    public void loadMods(){
        loadPlayerList(modsByUUID, moderators);
    }

    public void loadMems(){
        loadPlayerList(membersByUUID, members);
    }

    public void loadTMems(){
        loadPlayerList(totalMembersByUUID, totalMembers);
    }

    public void loadInvs(){
        loadPlayerList(invitesByUUID, invites);
    }

    public void loadPlayerList(List<String> uuidList, List<SavableUser> userList){
        if (uuidList == null) uuidList = new ArrayList<>();
        if (userList == null) userList = new ArrayList<>();

        userList.clear();

        for (String uuid : uuidList) {
            SavableUser user = PlayerUtils.getStatByUUID(uuid);

            if (user == null) continue;

            userList.add(user);
        }
    }

    private List<String> loadModerators(){
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("mods").equals("") || getFromKey("mods") == null) return uuids;
            if (! getFromKey("mods").contains(".")) {
                uuids.add(getFromKey("mods"));
                return uuids;
            }

            for (String uuid : getFromKey("mods").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return uuids;
    }

    private List<String> loadMembers() {
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("members").equals("") || getFromKey("members") == null) return uuids;
            if (! getFromKey("members").contains(".")) {
                uuids.add(getFromKey("members"));
                return uuids;
            }

            for (String uuid : getFromKey("members").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private List<String> loadTotalMembers() {
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("total-members").equals("") || getFromKey("total-members") == null) return uuids;
            if (! getFromKey("total-members").contains(".")) {
                uuids.add(getFromKey("total-members"));
                return uuids;
            }

            for (String uuid : getFromKey("total-members").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private List<String> loadInvites() {
        List<String> uuids = new ArrayList<>();

        try {
            if (getFromKey("invites").equals("") || getFromKey("invites") == null) return uuids;
            if (! getFromKey("invites").contains(".")) {
                uuids.add(getFromKey("invites"));
                return uuids;
            }

            for (String uuid : getFromKey("invites").split("\\.")) {
                try {
                    uuids.add(uuid);
                } catch (Exception e) {
                    //continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuids;
    }

    private String getInvitesAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (String uuid : invitesByUUID){
            if (i != invitesByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
            i++;
        }

        return builder.toString();
    }

    private String getTotalMembersAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : totalMembersByUUID){
            i++;
            if (i != totalMembersByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    private String getMembersAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : membersByUUID){
            i++;
            if (i != membersByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    private String getModeratorsAsStringed(){
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : modsByUUID){
            i++;
            if (i != modsByUUID.size()){
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public boolean hasMember(String uuid){
        return totalMembersByUUID.contains(uuid);
    }

    public boolean hasMember(SavableUser stat){
        loadMods();
        loadMems();
        loadTMems();
        loadInvs();

        return hasPMember(stat) || hasUUIDMember(stat);
    }

    public boolean hasPMember(SavableUser stat){
        try {
            return totalMembers.contains(stat);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasUUIDMember(SavableUser stat){
        try {
            return hasMember(stat.uuid);
        } catch (Exception e){
            return false;
        }
    }

    public boolean hasModPerms(String uuid) {
        try {
            return modsByUUID.contains(uuid) || leaderUUID.equals(uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasModPerms(SavableUser stat) {
        try {
            return moderators.contains(stat) || leaderUUID.equals(stat.uuid);
        } catch (Exception e) {
            return false;
        }
    }

    public int getSize(){
        return totalMembersByUUID.size();
    }

    public String removeFromModerators(SavableUser stat){
        modsByUUID.remove(stat.uuid);
        moderators.remove(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : modsByUUID) {
            i++;
            if (i != modsByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromMembers(SavableUser stat){
        membersByUUID.remove(stat.uuid);
        members.remove(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : membersByUUID) {
            i++;
            if (i != membersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromTMembers(SavableUser stat){
        totalMembersByUUID.remove(stat.uuid);
        totalMembers.remove(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : totalMembersByUUID) {
            i++;
            if (i != totalMembersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String remFromInvites(SavableUser from, SavableUser stat){
        invitesByUUID.remove(stat.uuid);
        invites.remove(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : invitesByUUID) {
            i++;
            if (i != invitesByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }
        
        updateKey("invites", builder.toString());
        
        pushInfo();

        return builder.toString();
    }

    public String addToModerators(SavableUser stat){
        modsByUUID.add(stat.uuid);
        moderators.add(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : modsByUUID) {
            i++;
            if (i != modsByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToMembers(SavableUser stat){
        membersByUUID.add(stat.uuid);
        members.add(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : membersByUUID) {
            i++;
            if (i != membersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToTMembers(SavableUser stat){
        totalMembersByUUID.add(stat.uuid);
        totalMembers.add(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : totalMembersByUUID) {
            i++;
            if (i != totalMembersByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public String addToInvites(SavableUser stat){
        invitesByUUID.add(stat.uuid);
        invites.add(stat);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String uuid : invitesByUUID) {
            i++;
            if (i != invitesByUUID.size()) {
                builder.append(uuid).append(".");
            } else {
                builder.append(uuid);
            }
        }

        return builder.toString();
    }

    public void addMember(SavableUser stat){
        updateKey("total-members", addToTMembers(stat));
        updateKey("members", addToMembers(stat));

        stat.updateKey("party", leaderUUID.toString());

        pushInfo();
    }

    public void removeMemberFromParty(SavableUser stat){
        Random RNG = new Random();

        if (leaderUUID.equals(stat.uuid)){
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("total-members", remFromTMembers(stat));
                    updateKey("members", remFromMembers(stat));
                    updateKey("mods", removeFromModerators(stat));
                    disband();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    SavableUser newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    SavableUser newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        updateKey("total-members", remFromTMembers(stat));
        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(stat));
        updateKey("mods", removeFromModerators(stat));
    }

    public void addInvite(SavableUser to) {
        updateKey("invites", addToInvites(to));
        loadVars();
    }

    public void toggleMute(){
        updateKey("muted", ! isMuted);
    }

    public void setPublic(boolean bool){
        updateKey("public", bool);
    }

    public Level getLevel(SavableUser member){
        if (this.membersByUUID.contains(member.uuid))
            return Level.MEMBER;
        else if (this.modsByUUID.contains(member.uuid))
            return Level.MODERATOR;
        else if (this.leaderUUID.equals(member.uuid))
            return Level.LEADER;
        else
            return Level.MEMBER;
    }

    public void setModerator(SavableUser stat) {
        Random RNG = new Random();

        forModeratorRemove(stat);

        if (leaderUUID.equals(stat.uuid)) {
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("total-members", remFromTMembers(stat));
                    updateKey("members", remFromMembers(stat));
                    updateKey("mods", removeFromModerators(stat));
                    disband();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    SavableUser newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    SavableUser newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        loadMods();
        loadMems();

        updateKey("leader", leaderUUID);
        updateKey("members", remFromMembers(stat));
        updateKey("mods", addToModerators(stat));

        pushInfo();
    }

    public void setMember(SavableUser stat) {
        Random RNG = new Random();

        forMemberRemove(stat);

        if (leaderUUID.equals(stat.uuid)) {
            if (totalMembers.size() <= 1) {
                try {
                    updateKey("total-members", remFromTMembers(stat));
                    updateKey("members", remFromMembers(stat));
                    updateKey("mods", removeFromModerators(stat));
                    disband();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                if (moderators.size() > 0) {
                    int r = RNG.nextInt(moderators.size());
                    SavableUser newLeader = moderators.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    modsByUUID.remove(newLeader.uuid);
                }
                if (members.size() > 0) {
                    int r = RNG.nextInt(members.size());
                    SavableUser newLeader = members.get(r);

                    totalMembersByUUID.remove(leaderUUID);
                    leaderUUID = newLeader.uuid;
                    membersByUUID.remove(newLeader.uuid);
                }
            }
        }

        loadMods();
        loadMems();

        updateKey("leader", leaderUUID);
        updateKey("members", addToMembers(stat));
        updateKey("mods", removeFromModerators(stat));

        pushInfo();
    }

    public void forModeratorRemove(SavableUser stat){
        this.modsByUUID.removeIf(m -> m.equals(stat.uuid));
        updateKey("mods", getModeratorsAsStringed());
    }

    public void forMemberRemove(SavableUser stat){
        this.membersByUUID.removeIf(m -> m.equals(stat.uuid));
        updateKey("members", getMembersAsStringed());
    }

    public void forTotalMembersRemove(SavableUser stat){
        this.totalMembersByUUID.removeIf(m -> m.equals(stat.uuid));
        updateKey("total-members", getTotalMembersAsStringed());
    }

    public void replaceLeader(SavableUser stat) {
        updateKey("mods", getModeratorsAsStringed() + "." + leaderUUID.toString());
        modsByUUID = loadModerators();
        updateKey("leader", stat.uuid);
        updateKey("mods", getModeratorsAsStringed()
                .replace("." + leaderUUID.toString(), "")
                .replace(leaderUUID.toString() + ".", "")
                .replace(leaderUUID.toString(), "")
        );

        leaderUUID = getFromKey("leader");

        loadMods();

        try {
            for (SavableUser p : totalMembers) {
                p.updateKey("party", leaderUUID.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pushInfo();
    }

//    public void setMaxSize(int size){
//        if (size < getMaxSize(this.leader))
//            this.maxSize = size;
//    }

    public void dispose() throws Throwable {
        this.leaderUUID = null;
        this.finalize();
    }

    public void disband(){
//        for (String uuid : totalMembersByUUID){
//            SavableUser stat = PlayerUtils.getOrCreateSUByUUID(uuid);
//
//            stat.updateKey("party", "");
//        }
//
//        PartyUtils.removeParty(this);
//
//        file.delete();
//
//        try {
//            dispose();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
    }

    public SavableUser getMemberAt(int at) {
        if (at > totalMembers.size() - 1) return totalMembers.get(totalMembers.size() - 1);
        if (at < 0) return totalMembers.get(0);

        return totalMembers.get(at);
    }
    
    public TreeSet<String> getInfoAsPropertyList() {
        TreeSet<String> infoList = new TreeSet<>();
        List<String> keys = new ArrayList<>();
        for (String key : info.keySet()){
            if (keys.contains(key)) continue;

            infoList.add(key + "=" + getFromKey(key));
            keys.add(key);
        }

        return infoList;
    }

    public void pushInfo() {
        savedKeys = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (String s : getInfoAsPropertyList()) {
            String key = s.split("=")[0];
            if (savedKeys.contains(key)) continue;
            savedKeys.add(key);

            strings.add(s + "\n");
        }

        if (PlayerUtils.onlinePlayers().size() > 0) {
            Messenger.sendPartyPluginMessageRequest(PlayerUtils.onlinePlayers().get(0), strings);
        }
    }
}
