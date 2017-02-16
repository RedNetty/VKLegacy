package io.vawke.practice.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.vawke.practice.Game;
import org.apache.commons.lang.Validate;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Giovanni on 28-5-2016.
 * <p>
 * A thread-safe asynchronous way of getting a Minecraft player's UUID.
 * Can be used with an ExecutorService or Bukkit's scheduler.
 */
public class UUIDCallback implements Callable<Map<String, UUID>> {

    private static final int MAX_SEARCH = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/page/";
    private static final String AGENT = "minecraft";
    private final JSONParser jsonParser = new JSONParser();
    private final List<String> names;

    private ScheduledExecutorService executorService;
    private BukkitScheduler bukkitScheduler;

    private boolean usingExecutorService = false;
    private boolean usingBukkitScheduler = false;

    public UUIDCallback(List<String> names, ScheduledExecutorService executorService) {
        this.names = ImmutableList.copyOf(names);
        Validate.notNull(executorService, "UUIDCallback<> executorService is null");
        this.executorService = executorService;
        this.usingExecutorService = executorService != null;
    }

    public UUIDCallback(List<String> names, BukkitScheduler bukkitScheduler) {
        this.names = ImmutableList.copyOf(names);
        Validate.notNull(executorService, "UUIDCallback<> bukkitScheduler is null");
        this.bukkitScheduler = bukkitScheduler;
        this.usingBukkitScheduler = bukkitScheduler != null;
    }

    public Map<String, UUID> call() throws Exception {
        return usingExecutorService ? this.execAsyncService() : this.execAsyncBukkit();
    }

    private Map<String, UUID> execAsyncBukkit() {
        Map<String, UUID> uuidMap = Maps.newHashMap();
        this.bukkitScheduler.scheduleAsyncDelayedTask(Game.getPracticeServer(), () -> {
            try {
                String body = buildBody(names);
                for (int i = 1; i < MAX_SEARCH; i++) {
                    HttpURLConnection connection = createConnection(i);
                    writeBody(connection, body);
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
                    JSONArray array = (JSONArray) jsonObject.get("profiles");
                    Number count = (Number) jsonObject.get("size");
                    if (count.intValue() == 0) break;
                    for (Object profile : array) {
                        JSONObject jsonProfile = (JSONObject) profile;
                        String id = (String) jsonProfile.get("id");
                        String name = (String) jsonProfile.get("name");
                        UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
                        uuidMap.put(name, uuid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return uuidMap;
    }

    // Unsafe
    private Map<String, UUID> execAsyncService() {
        Map<String, UUID> uuidMap = Maps.newHashMap();
        this.executorService.scheduleWithFixedDelay(() -> {
            try {
                String body = buildBody(names);
                for (int i = 1; i < MAX_SEARCH; i++) {
                    HttpURLConnection connection = createConnection(i);
                    writeBody(connection, body);
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
                    JSONArray array = (JSONArray) jsonObject.get("profiles");
                    Number count = (Number) jsonObject.get("size");
                    if (count.intValue() == 0) break;
                    for (Object profile : array) {
                        JSONObject jsonProfile = (JSONObject) profile;
                        String id = (String) jsonProfile.get("id");
                        String name = (String) jsonProfile.get("name");
                        UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
                        uuidMap.put(name, uuid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MILLISECONDS);
        return uuidMap;
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(body.getBytes());
        writer.flush();
        writer.close();
    }

    private static HttpURLConnection createConnection(int page) throws Exception {
        URL url = new URL(PROFILE_URL + page);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    private static String buildBody(List<String> names) {
        List<JSONObject> lookups = new ArrayList<JSONObject>();
        for (String name : names) {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("agent", AGENT);
            lookups.add(obj);
        }
        return JSONValue.toJSONString(lookups);
    }
}