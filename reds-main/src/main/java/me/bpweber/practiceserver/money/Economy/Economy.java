package me.bpweber.practiceserver.money.Economy;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.bpweber.practiceserver.PracticeServer;

public class Economy implements Listener {

	public static HashMap<UUID, Integer> currentBalance = new HashMap<UUID, Integer>();

	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
		PracticeServer.log.info("[Practice Server] Economy has been enabled");
		EcoConfig.setup();
		EcoConfig.get().options().copyDefaults(true);
		EcoConfig.save();
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
			EcoConfig.get().set(id.toString(), bal);
			EcoConfig.save();
		}
	}

	public void serverStart() {
		for (String key : EcoConfig.get().getKeys(false)) {
			int val = EcoConfig.get().getInt(key);
			UUID id = UUID.fromString(key);
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

	@EventHandler
	public void onLoginCheck(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (!(currentBalance.containsKey(p.getUniqueId()))) {
			currentBalance.put(p.getUniqueId(), 0);
		}
		new BukkitRunnable() {

			public void run() {
				int bal = 0;
				if (EcoConfig.get().contains(e.getPlayer().getUniqueId().toString())) {
					EcoConfig.get().addDefault(e.getPlayer().getUniqueId().toString(), bal);
					EcoConfig.save();
				}
			}
		}.runTaskLater(PracticeServer.plugin, 40);
	}
}
