package net.plasmere.streamlinehelper.config;

import net.plasmere.streamlinehelper.StreamLineHelper;
import net.plasmere.streamlinehelper.backendconf.Configuration;
import net.plasmere.streamlinehelper.backendconf.ConfigurationProvider;
import net.plasmere.streamlinehelper.backendconf.YamlConfiguration;
import net.plasmere.streamlinehelper.utils.Messenger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

public class ConfigHandler {
    private Configuration conf;
    private Configuration oConf;
    private Configuration mess;
    private Configuration oMess;

    private final String configVer = "1";
    private final String messagesVer = "1";

    //    private static final StreamLine inst = StreamLineHelper.instance;
    private final String cstring = "config.yml";
    private final File cfile = new File(StreamLineHelper.instance.getDataFolder(), cstring);
    private final String mstring = "messages.yml";
    private final File mfile = new File(StreamLineHelper.instance.getDataFolder(), mstring);

    public ConfigHandler(){
        if (! StreamLineHelper.instance.getDataFolder().exists()) {
            if (StreamLineHelper.instance.getDataFolder().mkdirs()) {
                Messenger.logInfo("Made folder: " + StreamLineHelper.instance.getDataFolder().getName());
            }
        }

//        this.configVer = StreamLineHelper.instance.getDescription().getVersion();
//        this.messagesVer = StreamLineHelper.instance.getDescription().getVersion();

//        System.out.println("config load - start");

        conf = loadConf();
        Messenger.logInfo("Loaded configuration!");
        mess = loadMess();
        Messenger.logInfo("Loaded messages!");

//        System.out.println("config load - end");
    }

//    public static Configuration getConf() { return conf; }
//    public static Configuration getMess() { return mess; }
//    public static Configuration getoConf() { return oConf; }
//    public static Configuration getoMess() { return oMess; }

    public void reloadConfig(){
        try {
            conf = loadConf();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reloadMessages(){
        try {
            mess = loadMess();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Configuration loadConf(){
        if (! cfile.exists()){
            try	(InputStream in = StreamLineHelper.instance.getResource(cstring)){
                Files.copy(in, cfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
            if (! this.configVer.equals(thing.getString("version"))){
                thing = iterateConfigs("oldconfig.yml");

                Messenger.logSevere("----------------------------------------------------------");
                Messenger.logSevere("YOU NEED TO UPDATE THE VALUES IN YOUR NEW CONFIG FILE AS");
                Messenger.logSevere("YOUR OLD ONE WAS OUTDATED. I IMPORTED THE NEW ONE FOR YOU.");
                Messenger.logSevere("----------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thing;
    }

    public Configuration loadMess(){
        if (! mfile.exists()){
            try	(InputStream in = StreamLineHelper.instance.getResource(mstring)){
                Files.copy(in, mfile.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Configuration thing = new Configuration();

        try {
            thing = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mfile);
            if (! this.messagesVer.equals(thing.getString("version"))){
                thing = iterateMessagesConf("oldmessages.yml");

                Messenger.logSevere("----------------------------------------------------------");
                Messenger.logSevere("YOU NEED TO UPDATE THE VALUES IN YOUR NEW MESSAGES FILE AS");
                Messenger.logSevere("YOUR OLD ONE WAS OUTDATED. I IMPORTED THE NEW ONE FOR YOU.");
                Messenger.logSevere("----------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thing;
    }

    private Configuration iterateConfigs(String old) throws IOException {
        File oldfile = new File(StreamLineHelper.instance.getDataFolder(), old);
        if (oldfile.exists()) {
            return iterateConfigs("new" + old);
        } else {
            try (InputStream in = StreamLineHelper.instance.getResource(cstring)) {
                Files.move(cfile.toPath(), oldfile.toPath());
                Files.copy(in, cfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            oConf = conf;
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
        }
    }

    private Configuration iterateMessagesConf(String old) throws IOException {
        File oldfile = new File(StreamLineHelper.instance.getDataFolder(), old);
        if (oldfile.exists()) {
            return iterateMessagesConf("new" + old);
        } else {
            try (InputStream in = StreamLineHelper.instance.getResource(mstring)) {
                Files.move(mfile.toPath(), oldfile.toPath());
                Files.copy(in, mfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            oMess = mess;
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(mfile);
        }
    }

    public void saveConf(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(conf, cfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMess(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(mess, mfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConfString(String path) {
        reloadConfig();
        return conf.getString(path);
    }

    public boolean getConfBoolean(String path) {
        reloadConfig();
        return conf.getBoolean(path);
    }

    public int getConfInteger(String path) {
        reloadConfig();
        return conf.getInt(path);
    }

    public List<String> getConfStringList(String path) {
        reloadConfig();
        return conf.getStringList(path);
    }

    public List<Integer> getConfIntegerList(String path) {
        reloadConfig();
        return conf.getIntList(path);
    }

    public Configuration getConfSection(String path) {
        reloadConfig();
        return conf.getSection(path);
    }

    public Object getObjectConf(String path){
        reloadConfig();
        return conf.get(path);
    }

    public void setObjectConf(String path, Object thing){
        conf.set(path, thing);
        reloadConfig();
    }

    public Collection<String> getConfKeys() {
        reloadConfig();
        return conf.getKeys();
    }

    public String getMessString(String path) {
        reloadMessages();
        return mess.getString(path);
    }

    public boolean getMessBoolean(String path) {
        reloadMessages();
        return mess.getBoolean(path);
    }

    public int getMessInteger(String path) {
        reloadMessages();
        return mess.getInt(path);
    }

    public List<String> getMessStringList(String path) {
        reloadMessages();
        return mess.getStringList(path);
    }

    public List<Integer> getMessIntegerList(String path) {
        reloadMessages();
        return conf.getIntList(path);
    }

    public Configuration getMessSection(String path) {
        reloadMessages();
        return mess.getSection(path);
    }

    public Object getObjectMess(String path){
        reloadMessages();
        return mess.get(path);
    }

    public void setObjectMess(String path, Object thing){
        mess.set(path, thing);
        reloadMessages();
    }

    public Collection<String> getMessKeys() {
        reloadMessages();
        return mess.getKeys();
    }
}
