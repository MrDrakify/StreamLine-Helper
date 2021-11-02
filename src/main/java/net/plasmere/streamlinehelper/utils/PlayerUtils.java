package net.plasmere.streamlinehelper.utils;

import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.objects.getting.SavableUser;
import net.plasmere.streamlinehelper.runnables.TryFindPlayerRunnable;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    public static List<BukkitRunnable> findPlayerRunnables = new ArrayList<>();
    public static List<SavableUser> loadedUsers = new ArrayList<>();

    public static SavableUser removeStat(SavableUser user) {
        loadedUsers.remove(user);

        return user;
    }

    public static List<Player> onlinePlayers() {
        return new ArrayList<>(StreamLineHelper.instance.getServer().getOnlinePlayers());
    }

    public static void updateAllPlayers(){
        for (Player player : onlinePlayers()) {
            Messenger.sendDisplayNameUpdate(player);
        }
    }

    public static Player findPlayerByUUID(String uuid) {
        for (Player player : onlinePlayers()) {
            if (player.getUniqueId().toString().equals(uuid)) return player;
        }

        return null;
    }

    public static Player findPlayerByName(String name) {
        for (Player player : onlinePlayers()) {
            if (player.getName().equals(name)) return player;
        }

        return null;
    }

    public static boolean doTeleport(String sender, String toU) {
        Player from = PlayerUtils.findPlayerByUUID(sender);
        Player to = PlayerUtils.findPlayerByUUID(toU);
        if (to == null) {
            return false;
        }

        if (from == null) {
            return false;
        }

        from.teleport(to);
        return true;
    }

    public static void purgePlayerFinders() {
        List<BukkitRunnable> toRemove = new ArrayList<>();

        for (BukkitRunnable runnable : findPlayerRunnables) {
            if (runnable.isCancelled()) toRemove.add(runnable);
        }

        for (BukkitRunnable runnable : toRemove) {
            findPlayerRunnables.remove(runnable);
        }
    }

    public static void removeAllPlayerFinders(){
        List<BukkitRunnable> toRemove = new ArrayList<>();

        for (BukkitRunnable runnable : findPlayerRunnables) {
            runnable.cancel();
            toRemove.add(runnable);
        }

        for (BukkitRunnable runnable : toRemove) {
            findPlayerRunnables.remove(runnable);
        }
    }

    public static void addNewRepeatingPlayerFinder(String from, String to) {
        purgePlayerFinders();

        if (! doTeleport(from, to)) {
            BukkitRunnable runnable = new TryFindPlayerRunnable(from, to);
            runnable.runTaskTimer(StreamLineHelper.instance, 0, 20);
            findPlayerRunnables.add(runnable);
        }
    }

    public static void pingPlayer(String toPingUUID) {
        Player player = findPlayerByUUID(toPingUUID);

        if (player == null) return;

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.RECORDS, 1f, 1f);
    }

    public static SavableUser getStatByUUID(String uuid) {
        for (SavableUser user : loadedUsers) {
            if (uuid.equals(user.uuid)) return user;
        }

        return null;
    }
}
