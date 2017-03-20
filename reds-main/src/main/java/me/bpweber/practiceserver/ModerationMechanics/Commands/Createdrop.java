package me.bpweber.practiceserver.ModerationMechanics.Commands;

import me.bpweber.practiceserver.drops.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class Createdrop implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("createdrop") && p.getName().equals("RedsEmporium")) {
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