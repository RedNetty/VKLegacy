package me.bpweber.practiceserver.Crates.Commands;

import me.bpweber.practiceserver.Crates.Nametag;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class giveNameTag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("giveNameTag") && sender.getName().equalsIgnoreCase("RedsEmporium") || sender instanceof ConsoleCommandSender) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null && p.isOnline()) {
                ItemStack item = Nametag.item_ownership_tag;
                item.setAmount(Integer.parseInt(args[1]));
                p.getInventory().addItem(item);
            }
        }

        return false;
    }


}
