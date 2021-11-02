package net.plasmere.streamlinehelper.objects.getting;

import java.util.ArrayList;
import java.util.List;

public class SavableConsole extends SavableUser {
    public List<String> savedKeys = new ArrayList<>();

    public SavableConsole(String[] lines) {
        super(lines);
    }

    @Override
    public void loadMoreVars() {
        // Nothing.
    }
}
