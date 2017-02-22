package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unban implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String rank;

		if (cmd.getName().equalsIgnoreCase("psunban")) {
			rank = "";
			if (Setrank.ranks.containsKey(p.getName())) {
				rank = Setrank.ranks.get(p.getName());
			}
			if (rank.equals("pmod") || p.isOp()) {
				if (args.length == 1) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
					if (Ban.banned.containsKey(target.getUniqueId())) {
						Ban.banned.remove(target.getUniqueId());
						p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "UNBANNED" + ChatColor.RED
								+ " player " + args[0]);
					}
				} else {
					p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED
							+ "/psunban <PLAYER>");
				}
			}
		}
		return false;
	}
}