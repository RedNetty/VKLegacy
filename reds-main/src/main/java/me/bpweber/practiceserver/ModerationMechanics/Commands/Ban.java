package me.bpweber.practiceserver.ModerationMechanics.Commands;

import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Ban implements CommandExecutor {

	public static ConcurrentHashMap<UUID, Integer> banned = new ConcurrentHashMap<UUID, Integer>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSender p;
		String rank;
		String player;
		block66: {
			p = sender;

			if (cmd.getName().equalsIgnoreCase("psban")) {
				rank = "";
				if (Setrank.ranks.containsKey(p.getName())) {
					rank = Setrank.ranks.get(p.getName());
				}
				if (rank.equals("pmod") || p.isOp() || sender instanceof ConsoleCommandSender) {
					if (args.length == 2) {
						player = args[0];
						Player target  = Bukkit.getPlayer(player);
						try {
							int seconds = Integer.parseInt(args[1]) * 60 * 60;
							banned.put(target.getUniqueId(), seconds);
							p.sendMessage(ChatColor.AQUA + "You have banned the user '" + args[0] + "' for "
									+ Integer.parseInt(args[1]) + " hours.");
							if (Bukkit.getServer().getPlayer(player) == null
									|| !Bukkit.getServer().getPlayer(player).isOnline())
								break block66;
							if (args[1] == "-1") {
								if (Alignments.tagged.containsKey(Bukkit.getServer().getPlayer(player).getName())) {
									Alignments.tagged.remove(Bukkit.getServer().getPlayer(player).getName());
								}
								Bukkit.getServer().getPlayer(player)
										.kickPlayer(ChatColor.RED + "Your account has been PERMANENTLY disabled." + "\n"
												+ ChatColor.GRAY
												+ "For further information about this suspension, please contact a "
												+ ChatColor.UNDERLINE + "Staff Member");
								break block66;
							}
							if (Alignments.tagged.containsKey(Bukkit.getServer().getPlayer(player).getName())) {
								Alignments.tagged.remove(Bukkit.getServer().getPlayer(player).getName());
							}
							Bukkit.getServer().getPlayer(player)
									.kickPlayer(ChatColor.RED
											+ "Your account has been TEMPORARILY locked due to suspisious activity."
											+ "\n" + ChatColor.GRAY
											+ "For further information about this suspension, please contact a "
											+ ChatColor.UNDERLINE + "Staff Member");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid time entired for hours of duration for the ban.");
							p.sendMessage(
									ChatColor.GRAY + "You entered: " + args[1] + ", which is not a numberic value.");
						}
					} else {
						p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED
								+ "/psban <PLAYER> <TIME(in hours)>");
						p.sendMessage(ChatColor.GRAY + "Insert -1 for <TIME> to permentantly lock.");
					}
				}
			}
		}
		return false;
	}
}