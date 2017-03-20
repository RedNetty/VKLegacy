package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Tellall implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if (cmd.getName().equalsIgnoreCase("tellall")) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }

                String allArgs = sb.toString().trim();
                Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">>> " + ChatColor.AQUA + allArgs);
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.playSound(pl.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            }
        }
        return false;
    }
}
