package me.bpweber.practiceserver.player.Stats;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
        StatsConfig.setup();
        StatsConfig.get().options().copyDefaults(true);
        StatsConfig.save();
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
            StatsConfig.get().set(id.toString() + ".Player Kills", val);
            StatsConfig.save();
        }
        for (UUID id : currentMonsterKills.keySet()) {
            int val = currentMonsterKills.get(id);
            StatsConfig.get().set(id.toString() + ".Monster Kills", val);
            StatsConfig.save();
        }
    }

    public void serverStart() {
        for (String key : StatsConfig.get().getKeys(false)) {
            int pkval = StatsConfig.get().getInt(key + ".Player Kills");
            int mkval = StatsConfig.get().getInt(key + ".Monster Kills");
            UUID id = UUID.fromString(key);
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


    @EventHandler
    public void onLoginCheck(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!(currentPlayerKills.containsKey(p.getUniqueId()))) {
            currentPlayerKills.put(p.getUniqueId(), 0);
        }
        if (!(currentMonsterKills.containsKey(p.getUniqueId()))) {
            currentMonsterKills.put(p.getUniqueId(), 0);
        }
        new BukkitRunnable() {

            public void run() {
                int def = 0;
                if (StatsConfig.get().contains(e.getPlayer().getUniqueId().toString())) {
                    StatsConfig.get().addDefault(e.getPlayer().getUniqueId().toString() + ".Player Kills", def);
                    StatsConfig.get().addDefault(e.getPlayer().getUniqueId().toString() + ".Monster Kills", def);
                    StatsConfig.save();
                }
            }
        }.runTaskLater(PracticeServer.plugin, 40);
    }
}
