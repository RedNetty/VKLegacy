package me.bpweber.practiceserver.DonationMechanics.Commands;

import me.bpweber.practiceserver.DonationMechanics.Crates.CratesMain;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class giveKey implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("giveKey") && sender.getName().equalsIgnoreCase("RedNetty") || sender instanceof ConsoleCommandSender || sender.getName().equalsIgnoreCase("Kayaba")) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null && p.isOnline()) {
                ItemStack item = CratesMain.createKey(false);
                item.setAmount(Integer.parseInt(args[1]));
                p.getInventory().addItem(item);
            }
        }

        return false;
    }


}
