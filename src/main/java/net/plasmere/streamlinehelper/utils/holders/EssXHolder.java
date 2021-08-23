package net.plasmere.streamlinehelper.utils.holders;

import com.earth2me.essentials.Essentials;
import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.utils.Messenger;

public class EssXHolder {
    public Essentials api;
    public boolean enabled;

    public EssXHolder(){
        enabled = isPresent();
    }

    public boolean isPresent(){
        if (StreamLineHelper.instance.pm.getPlugin("Essentials") == null) {
            return false;
        }

        try {
            api = (Essentials) StreamLineHelper.instance.pm.getPlugin("Essentials");
            return true;
        } catch (Exception e) {
            Messenger.logSevere("Essentials not loaded... Disabling Essentials support...");
        }
        return false;
    }
}
