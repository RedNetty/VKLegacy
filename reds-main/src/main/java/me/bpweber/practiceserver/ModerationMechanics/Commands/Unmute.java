package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Unmute implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String rank;
        String player;
        if (cmd.getName().equalsIgnoreCase("psunmute")) {
            rank = "";
            if (Setrank.ranks.containsKey(p.getName())) {
                rank = Setrank.ranks.get(p.getName());
            }
            if (rank.equals("pmod") || p.isOp()) {
                if (args.length == 1) {
                    if (Mute.muted.containsKey(args[0].toLowerCase())) {
                        Player m;
                        Mute.muted.remove(args[0].toLowerCase());
                        p.sendMessage(
                                ChatColor.AQUA + "You have " + ChatColor.BOLD + "UNMUTED " + ChatColor.AQUA + args[0]);
                        if (Bukkit.getPlayer((String) args[0]) != null
                                && (m = Bukkit.getPlayer((String) args[0])).isOnline()) {
                            m.sendMessage("");
                            m.sendMessage(ChatColor.GREEN + "Your " + ChatColor.BOLD + "GLOBAL MUTE" + ChatColor.GREEN
                                    + " has been removed.");
                            m.sendMessage("");
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED
                            + "/psunmute <PLAYER>");
                }
            }
        }
        return false;
    }
}