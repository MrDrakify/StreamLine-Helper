package net.plasmere.streamlinehelper.runnables;

import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.utils.PlayerUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class TryFindPlayerRunnable extends BukkitRunnable {

    private int cooldown;
    private final int reset;
    private String from;
    private String to;

    public TryFindPlayerRunnable(String from, String to){
        this.cooldown = 0;
        this.reset = 2; // 2 seconds.
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        if (this.cooldown <= 0) {
            done();
            this.cooldown = this.reset;
        }

        cooldown --;
    }

    private void done(){
        if (! PlayerUtils.doTeleport(from, to)) {
            PlayerUtils.addNewRepeatingPlayerFinder(from, to);
        }
        this.cancel();
    }
}
