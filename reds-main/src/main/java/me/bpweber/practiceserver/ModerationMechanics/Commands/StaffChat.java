package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.bpweber.practiceserver.ModerationMechanics.ModerationMechanics;

public class StaffChat implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
			Player p = (Player) sender;
			if (cmd.getName().equals("sc") && ModerationMechanics.isStaff(p)) {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (ModerationMechanics.isStaff(pl)) {
						StringBuilder builder = new StringBuilder();
						for (int i = 0; i < args.length; i++) {
							builder.append(args[i] + " ");
						}
						String msg = builder.toString();
						String prefix = ChatColor.GRAY + "<" + ChatColor.GOLD.toString() + ChatColor.BOLD + "SC"
								+ ChatColor.GRAY + "> ";
						pl.sendMessage(prefix + p.getDisplayName() + ": " + ChatColor.WHITE + msg);
					}
				}
			}
		}
		return false;
	}
}