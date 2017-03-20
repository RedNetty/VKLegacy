package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;

public class FixItem implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("dump")) {
            p.getInventory().setItemInHand(null);
        }
        return false;
    }
}
