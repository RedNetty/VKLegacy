package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.concurrent.*;

public class Mute implements CommandExecutor {
    public static ConcurrentHashMap<String, Integer> muted = new ConcurrentHashMap<String, Integer>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String rank;
        String player;
        if (cmd.getName().equalsIgnoreCase("psmute")) {
            rank = "";
            if (Setrank.ranks.containsKey(p.getName())) {
                rank = Setrank.ranks.get(p.getName());
            }
            if (rank.equals("pmod") || p.isOp()) {
                if (args.length == 2) {
                    player = args[0];
                    if (muted.containsKey(player.toLowerCase())) {
                        p.sendMessage(ChatColor.RED + "You cannot " + ChatColor.UNDERLINE + "overwrite" + ChatColor.RED
                                + " a players mute.");
                    } else {
                        try {
                            muted.put(player.toLowerCase(), Integer.parseInt(args[1]) * 60);
                            p.sendMessage(ChatColor.AQUA + "You have issued a " + args[1] + " minute " + ChatColor.BOLD
                                    + "MUTE" + ChatColor.AQUA + " on the user " + ChatColor.BOLD + player);
                            p.sendMessage(
                                    ChatColor.GRAY + "If this was made in error, type '/psunmute " + player + "'");
                            if (Bukkit.getServer().getPlayer(player) != null
                                    && Bukkit.getServer().getPlayer(player).isOnline()) {
                                Bukkit.getServer().getPlayer(player).sendMessage("");
                                Bukkit.getServer().getPlayer(player)
                                        .sendMessage(ChatColor.RED + "You have been " + ChatColor.BOLD
                                                + "GLOBALLY MUTED" + ChatColor.RED + " by " + ChatColor.BOLD
                                                + p.getName() + ChatColor.RED + " for " + args[1] + " minute(s).");
                                Bukkit.getServer().getPlayer(player).sendMessage("");
                            }
                        } catch (Exception e) {
                            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Non-Numeric Time. "
                                    + ChatColor.RED + "/psmute <PLAYER> <TIME(in minutes)>");
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED
                            + "/psmute <PLAYER> <TIME(in minutes)>");
                }
            }
        }
        return false;
    }
}