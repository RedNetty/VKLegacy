package me.bpweber.practiceserver.money.Commands;

import me.bpweber.practiceserver.money.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Givepouch implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givePouch") && sender.getName().equalsIgnoreCase("RedNetty") || sender instanceof ConsoleCommandSender) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null && p.isOnline()) {
                p.getInventory().addItem(GemPouches.gemPouch(Integer.parseInt(args[1])));
            }
        }

        return false;
    }


}
