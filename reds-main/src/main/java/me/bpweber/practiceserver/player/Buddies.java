package me.bpweber.practiceserver.player;

import me.bpweber.practiceserver.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.io.*;
import java.util.*;

public class Buddies
        implements Listener,
        CommandExecutor {
    public static HashMap<String, ArrayList<String>> buddies = new HashMap<String, ArrayList<String>>();

    public void onEnable() {
        PracticeServer.log.info("[Buddies] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        File file = new File(PracticeServer.plugin.getDataFolder(), "buddies.yml");
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
        for (String p : config.getKeys(false)) {
            ArrayList<String> buddy = new ArrayList<String>();
            for (String t : config.getStringList(p)) {
                buddy.add(t);
            }
            buddies.put(p, buddy);
        }
    }

    public void onDisable() {
        PracticeServer.log.info("[Buddies] has been disabled.");
        File file = new File(PracticeServer.plugin.getDataFolder(), "buddies.yml");
        YamlConfiguration config = new YamlConfiguration();
        for (String s : buddies.keySet()) {
            config.set(s, buddies.get(s));
        }
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            ArrayList<String> buddy;
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("add")) {
                if (args.length > 0) {
                    buddy = Buddies.getBuddies(p.getName());
                    if (!buddy.contains(args[0].toLowerCase())) {
                        if (args[0].equalsIgnoreCase(p.getName())) {
                            p.sendMessage(ChatColor.YELLOW + "You can't add yourself to your buddy list!");
                        } else {
                            buddy.add(args[0].toLowerCase());
                            p.sendMessage(ChatColor.GREEN + "You've added " + ChatColor.BOLD + args[0] + ChatColor.GREEN + " to your BUDDY list.");
                            buddies.put(p.getName(), buddy);
                        }
                    } else {
                        p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + args[0] + ChatColor.YELLOW + " is already on your BUDDY LIST.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Incorrect Syntax - " + ChatColor.BOLD + "/add <PLAYER>");
                }
            }
            if (cmd.getName().equalsIgnoreCase("del") || cmd.getName().equalsIgnoreCase("delete")) {
                if (args.length > 0) {
                    buddy = Buddies.getBuddies(p.getName());
                    if (buddy.contains(args[0].toLowerCase())) {
                        buddy.remove(args[0].toLowerCase());
                        p.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.YELLOW + " has been removed from your BUDDY list.");
                        buddies.put(p.getName(), buddy);
                    } else {
                        p.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.YELLOW + " is not on any of your social lists.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Incorrect Syntax - " + ChatColor.BOLD + "/delete <PLAYER>");
                }
            }
        }
        return true;
    }

    public static ArrayList<String> getBuddies(String s) {
        if (buddies.containsKey(s)) {
            return buddies.get(s);
        }
        return new ArrayList<String>();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOp()) {
            return;
        }
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (!buddies.containsKey(pl.getName()) || !(Buddies.getBuddies(pl.getName())).contains(p.getName().toLowerCase()))
                continue;
            pl.sendMessage(ChatColor.YELLOW + p.getName() + " has joined this server.");
            pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 1.2f);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.isOp()) {
            return;
        }
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (!buddies.containsKey(pl.getName()) || !(Buddies.getBuddies(pl.getName())).contains(p.getName().toLowerCase()))
                continue;
            pl.sendMessage(ChatColor.YELLOW + p.getName() + " has logged out.");
            pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 0.5f);
        }
    }
}

