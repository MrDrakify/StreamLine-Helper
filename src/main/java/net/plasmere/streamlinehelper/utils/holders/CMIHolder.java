package net.plasmere.streamlinehelper.utils.holders;

import com.Zrips.CMI.CMI;
import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.utils.Messenger;

public class CMIHolder {
    public CMI api;
    public boolean enabled;

    public CMIHolder(){
        enabled = isPresent();
    }

    public boolean isPresent(){
        if (StreamLineHelper.instance.pm.getPlugin("CMI") == null) {
            return false;
        }

        try {
            api = CMI.getInstance();
            return true;
        } catch (Exception e) {
            Messenger.logSevere("CMI not loaded... Disabling CMI support...");
        }
        return false;
    }
}
