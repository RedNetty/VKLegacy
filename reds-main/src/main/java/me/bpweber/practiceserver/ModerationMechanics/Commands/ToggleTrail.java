package me.bpweber.practiceserver.ModerationMechanics.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bpweber.practiceserver.ModerationMechanics.ModerationMechanics;

public class ToggleTrail implements CommandExecutor {

	public static List<String> toggletrail = new ArrayList<String>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("toggletrail") && ModerationMechanics.isSub(p)) {
			if (toggletrail.contains(p.getName().toLowerCase())) {
				toggletrail.remove(p.getName().toLowerCase());
				p.sendMessage(ChatColor.GREEN + "Subscriber Trail - " + ChatColor.BOLD + "ENABLED");
			} else {
				toggletrail.add(p.getName().toLowerCase());
				p.sendMessage(ChatColor.RED + "Subscriber Trail - " + ChatColor.BOLD + "DISABLED");
			}
		}
		return false;
	}
}