package me.bpweber.practiceserver.player.Stats;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.GamePlayer.GameConfig;
import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jaxon on 3/18/2017.
 */
public class StatsMain implements Listener {
    public static HashMap<UUID, Integer> currentPlayerKills = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Integer> currentMonsterKills = new HashMap<UUID, Integer>();


    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        PracticeServer.log.info("[Practice Server] Stats Tracker has been enabled");
        serverStart();
        new BukkitRunnable() {

            public void run() {

            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 800, 800);

    }

    public void onDisable() {
        PracticeServer.log.info("[Practice Server] Stats Tracker has been disabled");
        saveAllStats();

    }

    public void saveAllStats() {
        for (UUID id : currentPlayerKills.keySet()) {
            int val = currentPlayerKills.get(id);
            GameConfig.get().set(id + ".Stats.Player Kills", val);

        }
        for (UUID id : currentMonsterKills.keySet()) {
            int val = currentMonsterKills.get(id);
            GameConfig.get().set(id.toString() + ".Stats.Monster Kills", val);

        }
    }

    public void serverStart() {
        for (String ke : GameConfig.get().getKeys(false)) {
            UUID key = UUID.fromString(ke);
            int pkval = GameConfig.get().getInt(key + ".Stats.Player Kills");
            int mkval = GameConfig.get().getInt(key + ".Stats.Monster Kills");
            UUID id = key;
            currentPlayerKills.put(id, pkval);
            currentMonsterKills.put(id, mkval);
        }

    }

    public static int getPlayerKills(UUID id) {
        if (currentPlayerKills.containsKey(id)) {
            int kills = currentPlayerKills.get(id);
            return kills;
        } else {
            return 0;
        }
    }

    public static int getMonsterKills(UUID id) {
        if (currentMonsterKills.containsKey(id)) {
            int kills = currentMonsterKills.get(id);
            return kills;
        } else {
            return 0;
        }
    }

    public static String getAlignment(Player p) {
        if (Alignments.chaotic.containsKey(p.getName())) {
            return ChatColor.RED.toString() + ChatColor.BOLD.toString() + "CHAOTIC";
        }
        if (Alignments.neutral.containsKey(p.getName())) {
            return ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "NEUTRAL";
        } else {
            return ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "LAWFUL";
        }
    }

    public static int getAlignTime(Player p) {
        if (Alignments.chaotic.containsKey(p.getName())) {
            return Alignments.chaotic.get(p.getName());
        }
        if (Alignments.neutral.containsKey(p.getName())) {
            return Alignments.neutral.get(p.getName());
        } else {
            return 0;
        }
    }
}
