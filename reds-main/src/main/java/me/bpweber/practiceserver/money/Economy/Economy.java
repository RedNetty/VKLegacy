package me.bpweber.practiceserver.money.Economy;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.GamePlayer.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Economy implements Listener {

    public static HashMap<UUID, Integer> currentBalance = new HashMap<UUID, Integer>();

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        PracticeServer.log.info("[Practice Server] Economy has been enabled");
        serverStart();
        new BukkitRunnable() {

            public void run() {

            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 800, 800);

    }

    public void onDisable() {
        PracticeServer.log.info("[Practice Server] Economy has been disabled");
        saveAllBalance();

    }

    public void saveAllBalance() {
        for (UUID id : currentBalance.keySet()) {
            int bal = currentBalance.get(id);
            GameConfig.get().set(id.toString() + ".Economy.Money Balance", bal);
            GameConfig.save();

        }
    }

    public void serverStart() {
        for (String key : GameConfig.get().getKeys(false)) {
            UUID id = UUID.fromString(key);
            int val = GameConfig.get().getInt(id + ".Economy.Money Balance");
            currentBalance.put(id, val);
        }

    }

    public static int getBalance(UUID id) {
        if (currentBalance.containsKey(id)) {
            int balance = currentBalance.get(id);
            return balance;
        } else {
            return 0;
        }
    }

    public static void depositPlayer(UUID id, int amount) {
        if (currentBalance.containsKey(id)) {
            int cB = currentBalance.get(id);
            int nB = cB + amount;
            currentBalance.remove(id);
            currentBalance.put(id, nB);
        } else {
            currentBalance.put(id, amount);
        }
    }

    public static void withdrawPlayer(UUID id, int amount) {
        if (currentBalance.containsKey(id)) {
            int cB = currentBalance.get(id);
            int nB = cB - amount;
            currentBalance.remove(id);
            currentBalance.put(id, nB);
        }
    }
}