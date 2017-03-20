package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class Vanish implements CommandExecutor {
    public static List<String> vanished = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("psvanish") && p.isOp()) {
            if (vanished.contains(p.getName().toLowerCase())) {
                vanished.remove(p.getName().toLowerCase());
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl == p)
                        continue;
                    pl.showPlayer(p);
                }
                p.sendMessage(ChatColor.RED + "You are now " + ChatColor.BOLD + "visible.");
            } else {
                vanished.add(p.getName().toLowerCase());
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl == p || pl.isOp())
                        continue;
                    pl.hidePlayer(p);
                }
                p.sendMessage(ChatColor.GREEN + "You are now " + ChatColor.BOLD + "invisible.");
            }
        }
        return false;
    }
}