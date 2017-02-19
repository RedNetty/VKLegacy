package me.bpweber.practiceserver.ModerationMechanics.Commands;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import me.bpweber.practiceserver.pvp.Alignments;

public class Setrank implements CommandExecutor {
	public static ConcurrentHashMap<String, String> ranks = new ConcurrentHashMap<String, String>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setrank") && sender.isOp() || sender instanceof ConsoleCommandSender) {
			if (args.length == 2) {
				String player2 = args[0];
				String r = args[1].toLowerCase();
				if (r.equals("pmod") || r.equals("sub") || r.equals("sub+") || r.equals("sub++")
						|| r.equals("default")) {
					if (r.equals("default")) {
						if (ranks.containsKey(player2)) {
							ranks.remove(player2);
						}
					} else {
						ranks.put(player2, r);
					}
					if (Bukkit.getServer().getPlayer(player2) != null) {
						sender.sendMessage(ChatColor.GREEN + "You have set the user " + player2 + " to the rank of " + r
								+ " on all Autism Realms servers.");
						Alignments.updatePlayerAlignment(Bukkit.getServer().getPlayer(player2));
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