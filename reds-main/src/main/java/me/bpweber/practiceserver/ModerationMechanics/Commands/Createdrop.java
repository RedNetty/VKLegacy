package me.bpweber.practiceserver.ModerationMechanics.Commands;

import me.bpweber.practiceserver.drops.CreateDrop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Createdrop implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("createdrop") && p.getName().equals("Fatherhood")) {
            if (args.length == 3) {
				try {
					p.getInventory().addItem(new ItemStack(CreateDrop.createDrop(Integer.parseInt(args[0]),
							Integer.parseInt(args[1]), Integer.parseInt(args[2]))));
				} catch (Exception e2) {
					p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD)
							.append("Incorrect Syntax: ").append(ChatColor.RED)
							.append("/createdrop <tier> <item> <rarity>").toString());
				}
			} else {
				p.sendMessage(
						new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Incorrect Syntax: ")
								.append(ChatColor.RED).append("/createdrop <tier> <item> <rarity>").toString());
			}
		}
		return false;
	}
}