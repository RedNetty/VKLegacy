package me.bpweber.practiceserver.player.GamePlayer;

import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameConfig {
    static FileConfiguration gameFile;
    static File gpfile;

    public static void setup() {
        gpfile = new File(PracticeServer.plugin.getDataFolder(), "GamePlayer.yml");

        if (!gpfile.exists()) {
            try {
                gpfile.createNewFile();
            } catch (IOException e) {
            }
        }

        gameFile = YamlConfiguration.loadConfiguration(gpfile);
    }

    public static FileConfiguration get() {
        return gameFile;
    }

    public static void save() {
        try {
            gameFile.save(gpfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save GamePlayer.yml!");
        }
    }

    public static void reload() {
        gameFile = YamlConfiguration.loadConfiguration(gpfile);
    }

}