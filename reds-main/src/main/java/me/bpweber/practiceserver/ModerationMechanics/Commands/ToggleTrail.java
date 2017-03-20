package me.bpweber.practiceserver.ModerationMechanics.Commands;

import me.bpweber.practiceserver.ModerationMechanics.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class ToggleTrail implements CommandExecutor {

    public static List<String> toggletrail = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("toggletrail") && ModerationMechanics.isSub(p)) {
            if (toggletrail.contains(p.getName().toLowerCase())) {
                toggletrail.remove(p.getName().toLowerCase());
                p.sendMessage(ChatColor.GREEN + "Subscriber Trail - " + ChatColor.BOLD + "ENABLED");
            } else {
                toggletrail.add(p.getName().toLowerCase());
                p.sendMessage(ChatColor.RED + "Subscriber Trail - " + ChatColor.BOLD + "DISABLED");
            }
        }
        return false;
    }
}