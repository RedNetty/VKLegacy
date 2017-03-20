package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class Invsee implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (cmd.getName().equalsIgnoreCase("Invsee") && p.isOp()) {
            if (args.length == 1) {

                if (target.isOnline()) {
                    Inventory targetInv = target.getInventory();
                    p.openInventory(targetInv);
                } else {
                    p.sendMessage(ChatColor.RED + args[0] + " is not online!");
                }
            } else {
                p.sendMessage(ChatColor.RED + "/invsee <player>");
            }
        }
        return false;
    }
}