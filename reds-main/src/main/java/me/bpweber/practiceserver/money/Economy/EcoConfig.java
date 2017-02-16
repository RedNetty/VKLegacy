package me.bpweber.practiceserver.money.Economy;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.bpweber.practiceserver.PracticeServer;

public class EcoConfig {
	static FileConfiguration EcoFile;
	static File pdfile;

	public static void setup() {
		pdfile = new File(PracticeServer.plugin.getDataFolder(), "Economy.yml");

		if (!pdfile.exists()) {
			try {
				pdfile.createNewFile();
			} catch (IOException e) {
			}
		}

		EcoFile = YamlConfiguration.loadConfiguration(pdfile);
	}

	public static FileConfiguration get() {
		return EcoFile;
	}

	public static void save() {
		try {
			EcoFile.save(pdfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Economy.yml!");
		}
	}

	public static void reload() {
		EcoFile = YamlConfiguration.loadConfiguration(pdfile);
	}

}