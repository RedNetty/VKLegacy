
package me.bpweber.practiceserver.ModerationMechanics;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Ban;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Mute;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;

public class ModerationMechanics {
	public Setrank setrank;

	public void onEnable() {
		int time;
		PracticeServer.log.info("[ModerationMechanics] has been enabled.");
		new BukkitRunnable() {

			public void run() {
				for (String s2 : Mute.muted.keySet()) {
					if (s2 == null)
						continue;
					if (Mute.muted.get(s2) <= 0) {
						Player p;
						Mute.muted.remove(s2);
						if (Bukkit.getPlayer((String) s2) == null || !(p = Bukkit.getPlayer((String) s2)).isOnline())
							continue;
						p.sendMessage(ChatColor.GREEN + "Your " + ChatColor.BOLD + "GLOBAL MUTE" + ChatColor.GREEN
								+ " has expired.");
						continue;
					}
					Mute.muted.put(s2, Mute.muted.get(s2) - 1);
				}
				for (String s2 : Ban.banned.keySet()) {
					if (s2 == null)
						continue;
					if (Ban.banned.get(s2) < 0) {
						Ban.banned.put(s2, -1);
						continue;
					}
					if (Ban.banned.get(s2) == 0) {
						Ban.banned.remove(s2);
						continue;
					}
					Ban.banned.put(s2, Ban.banned.get(s2) - 1);
				}
			}
		}.runTaskTimerAsynchronously(PracticeServer.plugin, 20, 20);
		File file = new File(PracticeServer.plugin.getDataFolder(), "bans.yml");
		YamlConfiguration config = new YamlConfiguration();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			config.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (config.getConfigurationSection("banned") != null) {
			for (String key : config.getConfigurationSection("banned").getKeys(false)) {
				time = config.getConfigurationSection("banned").getInt(key);
				Ban.banned.put(key, time);
			}
		}
		if (config.getConfigurationSection("muted") != null) {
			for (String key : config.getConfigurationSection("muted").getKeys(false)) {
				time = config.getConfigurationSection("muted").getInt(key);
				Mute.muted.put(key, time);
			}
		}
		this.loadranks();
	}

	public void onDisable() {
		PracticeServer.log.info("[ModerationMechanics] has been disabled.");
		File file = new File(PracticeServer.plugin.getDataFolder(), "bans.yml");
		YamlConfiguration config = new YamlConfiguration();
		for (String s2 : Ban.banned.keySet()) {
			config.set("banned." + s2, Ban.banned.get(s2));
		}
		for (String s2 : Mute.muted.keySet()) {
			config.set("muted." + s2, Mute.muted.get(s2));
		}
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.saveranks();
	}

	public static boolean isSub(Player p) {
		String rank;
		if (Setrank.ranks.containsKey(p.getName()) && ((rank = Setrank.ranks.get(p.getName())).equalsIgnoreCase("sub")
				|| rank.equalsIgnoreCase("sub+") || rank.equalsIgnoreCase("sub++") || p.isOp())) {
			return true;
		}
		return false;
	}

	public static boolean isStaff(Player p) {

		if (Setrank.ranks.containsKey(p.getName()) && (Setrank.ranks.get(p.getName())).equalsIgnoreCase("PMOD")
				|| p.isOp()) {
			return true;
		}
		return false;
	}

	void loadranks() {
		File file = new File(PracticeServer.plugin.getDataFolder(), "ranks.yml");
		YamlConfiguration config = new YamlConfiguration();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			config.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (config.getConfigurationSection("ranks") != null) {
			for (String key : config.getConfigurationSection("ranks").getKeys(false)) {
				String p = config.getConfigurationSection("ranks").getString(key);
				Setrank.ranks.put(key, p);
			}
		}
	}

	void saveranks() {
		File file = new File(PracticeServer.plugin.getDataFolder(), "ranks.yml");
		YamlConfiguration config = new YamlConfiguration();
		for (String s : Setrank.ranks.keySet()) {
			config.set("ranks." + s, Setrank.ranks.get(s));
		}
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}