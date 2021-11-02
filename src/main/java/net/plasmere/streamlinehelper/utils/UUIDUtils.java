package net.plasmere.streamlinehelper.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class UUIDUtils {
    public static Cache<String, String> cachedUUIDs = Caffeine.newBuilder().build();
    public static Cache<String, String> cachedNames = Caffeine.newBuilder().build();

    public static String getCachedUUID(String username) {
        if (username.equals("%")) return username;
        if (username.contains("-")) return username;

//        if (ConfigUtils.offlineMode()) {
//            return StreamLine.offlineStats.getUUID(username);
//        }

        try {
            String finalUsername = username.replace("\"", "").toLowerCase(Locale.ROOT);
            return cachedUUIDs.get(username, (u) -> fetch(finalUsername));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }

    public static String getCachedName(String uuid) {
        if (uuid.equals("%")) return uuid;
        if (! uuid.contains("-")) return uuid;

//        if (ConfigUtils.offlineMode()) {
//            return StreamLine.offlineStats.getPlayerName(uuid);
//        }

        try {
            return Objects.requireNonNull(cachedNames.get(uuid, (u) -> getName(uuid))).replace("\"", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }

    static public String fetch(String username) {
        if (username.contains("-")) return getName(username);

//        if (ConfigUtils.offlineMode()) {
//            return StreamLine.offlineStats.getUUID(username);
//        }

//        try {
//            if (StreamLine.geyserHolder.enabled) {
//                if (StreamLine.geyserHolder.isGeyserPlayer(username)) {
//                    return StreamLine.geyserHolder.file.getUUID(username);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        username = username.toLowerCase(Locale.ROOT);
        try {
            String JSONString = "";

            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                JSONString = line;
            }

            JsonElement obj = new JsonParser().parse(JSONString);

            JsonObject jo = (JsonObject) obj;

            String id = jo.get("id").getAsString();

            String uuid = formatToUUID(id);

            return uuid;
            //return UUID.fromString(id);
        } catch (Exception e){
            Messenger.logInfo("Error on Username of: " + username);
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }

    public static String getName(String uuid) {
        if (! uuid.contains("-")) return fetch(uuid);

//        if (ConfigUtils.offlineMode()) {
//            return StreamLine.offlineStats.getPlayerName(uuid);
//        }

//        try {
//            if (StreamLine.geyserHolder.enabled) {
//                String name = StreamLine.geyserHolder.file.getName(uuid);
//                if (name != null) if (! name.equals("")) return name;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            String JSONString = "";

            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                JSONString = line;
            }

            Object obj = new JsonParser().parse(JSONString);
            JsonArray jo = (JsonArray) obj;
            String last = jo.get(jo.size() - 1).toString();
            Object job = new JsonParser().parse(last);
            JsonObject njo = (JsonObject) job;

            return njo.get("name").toString();
        } catch (Exception e){
            Messenger.logInfo("Error on UUID of: " + uuid);
            e.printStackTrace();
        }
        return "error";
    }

    public static String formatToUUID(String unformatted){
        StringBuilder formatted = new StringBuilder();
        int i = 1;
        for (Character character : unformatted.toCharArray()){
            if (i == 9 || i == 13 || i == 17 || i == 21){
                formatted.append("-").append(character);
            } else {
                formatted.append(character);
            }
            i++;
        }

        return formatted.toString();
    }

    public static String swapUUID(String uuid){
        if (uuid.contains("-")){
            return uuid.replace("-", "");
        } else {
            return formatToUUID(uuid);
        }
    }

    public static String swapToUUID(String thingThatMightBeAName){
        String uuid = thingThatMightBeAName;

        if (! thingThatMightBeAName.contains("-") && ! (thingThatMightBeAName.equals("%"))) {
            uuid = getCachedUUID(thingThatMightBeAName);
        }

        return uuid;
    }

    public static String swapToName(String thingThatMightBeAUUID){
        String name = thingThatMightBeAUUID;

//        if (thingThatMightBeAUUID.equals("%")) {
//            return ConfigUtils.consoleName();
//        }

        if (thingThatMightBeAUUID.contains("-")) {
            name = getCachedName(thingThatMightBeAUUID);
        }

        return name;
    }
}
