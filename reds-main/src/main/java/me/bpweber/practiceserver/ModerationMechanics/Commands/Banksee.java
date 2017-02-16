package me.bpweber.practiceserver.ModerationMechanics.Commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.money.Banks;

public class Banksee implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("banksee") && p.isOp()) {
			if (args.length == 1) {
				File file = new File(PracticeServer.plugin.getDataFolder() + "/banks", String.valueOf(args[0]) + ".yml");
				if (file.exists()) {
					Banks.banksee.put(p, args[0]);
					Inventory inv = Banks.getBank(p);
					if (inv == null) {
						inv = Bukkit.createInventory((InventoryHolder) null, (int) 63, (String) "Bank Chest (1/1)");
					}
					p.openInventory(inv);
					p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
				} else {
					p.sendMessage(ChatColor.RED + args[0] + " does not have a bank!");
				}
			} else {
				p.sendMessage(ChatColor.RED + "/banksee <player>");
			}
		}
		return false;
	}
}