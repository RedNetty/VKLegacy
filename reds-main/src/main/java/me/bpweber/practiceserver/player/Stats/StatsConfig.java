package me.bpweber.practiceserver.player.Stats;

import me.bpweber.practiceserver.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

import java.io.*;

public class StatsConfig {
    static FileConfiguration statFile;
    static File stfile;

    public static void setup() {
        stfile = new File(PracticeServer.plugin.getDataFolder(), "PlayerStats.yml");

        if (!stfile.exists()) {
            try {
                stfile.createNewFile();
            } catch (IOException e) {
            }
        }

        statFile = YamlConfiguration.loadConfiguration(stfile);
    }

    public static FileConfiguration get() {
        return statFile;
    }

    public static void save() {
        try {
            statFile.save(stfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save PlayerStats.yml!");
        }
    }

    public static void reload() {
        statFile = YamlConfiguration.loadConfiguration(stfile);
    }

}