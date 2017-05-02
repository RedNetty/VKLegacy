package me.bpweber.practiceserver.ModerationMechanics.Commands;

import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Setrank implements CommandExecutor {
    public static HashMap<UUID, String> ranks = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setrank") && sender.isOp() || sender instanceof ConsoleCommandSender) {
            if (args.length == 2) {
                Player player2 = Bukkit.getPlayer(args[0]);
                String r = args[1].toLowerCase();
                if (r.equalsIgnoreCase("pmod") || r.equalsIgnoreCase("sub") || r.equalsIgnoreCase("sub+") || r.equalsIgnoreCase("sub++")
                        || r.equalsIgnoreCase("default")) {
                    if (r.equalsIgnoreCase("default")) {
                        if (ranks.containsKey(player2.getUniqueId())) {
                            ranks.remove(player2.getUniqueId());
                        }
                    } else {
                        ranks.put(player2.getUniqueId(), r);
                    }
                    if (Bukkit.getServer().getPlayer(player2.getUniqueId()) != null) {
                        sender.sendMessage(ChatColor.GREEN + "You have set the user " + player2.getName() + " to the rank of " + r
                                + " on all Dungeon Isles servers.");
                        Alignments.updatePlayerAlignment(player2);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax");
                    sender.sendMessage(ChatColor.RED + "/setrank <PLAYER> <RANK>");
                    sender.sendMessage(
                            ChatColor.RED + "Ranks: " + ChatColor.GRAY + "Default | SUB | SUB+ | SUB++ | PMOD");
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "/setrank <PLAYER> <RANK>");
                sender.sendMessage(ChatColor.RED + "Ranks: " + ChatColor.GRAY + "Default | SUB | SUB+ | SUB++ | PMOD");
            }
        }
        return false;
    }
}