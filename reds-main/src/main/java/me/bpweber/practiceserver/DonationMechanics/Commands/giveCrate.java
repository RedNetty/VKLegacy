package me.bpweber.practiceserver.DonationMechanics.Commands;

import me.bpweber.practiceserver.DonationMechanics.Crates.CratesMain;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class giveCrate implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("giveCrate") && sender.getName().equalsIgnoreCase("RedsEmporium") || sender instanceof ConsoleCommandSender) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null && p.isOnline()) {
                p.getInventory().addItem(CratesMain.createCrate(Integer.parseInt(args[1])));
            }
        }

        return false;
    }


}
