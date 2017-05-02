package me.bpweber.practiceserver.world.Tips;

import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TipsConfig {
    static FileConfiguration tipsFile;
    static File tfile;

    public static void setup() {
        tfile = new File(PracticeServer.plugin.getDataFolder(), "Tips.yml");

        if (!tfile.exists()) {
            try {
                tfile.createNewFile();
            } catch (IOException e) {
            }
        }

        tipsFile = YamlConfiguration.loadConfiguration(tfile);

    }

    public static FileConfiguration get() {
        return tipsFile;
    }

    public static void save() {
        try {
            tipsFile.save(tfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Tips.yml!");
        }
    }

    public static void reload() {
        tipsFile = YamlConfiguration.loadConfiguration(tfile);
    }

}