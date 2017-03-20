package me.bpweber.practiceserver.money.Economy;

import me.bpweber.practiceserver.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

import java.io.*;

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