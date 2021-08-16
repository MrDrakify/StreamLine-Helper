package net.plasmere.streamlinehelper.runnables;

import net.plasmere.streamlinehelper.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OneSecondRunnable extends BukkitRunnable {

    private int cooldown;
    private final int reset;

    public OneSecondRunnable(/*int reset*/){
        this.cooldown = 0;
        this.reset = 1;
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
        PlayerUtils.updateAllPlayers();
    }
}
