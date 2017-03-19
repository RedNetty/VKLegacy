package me.bpweber.practiceserver.money.Commands;

import me.bpweber.practiceserver.money.GemPouches;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Givepouch implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givePouch") && sender.getName().equalsIgnoreCase("RedsEmporium") || sender instanceof ConsoleCommandSender) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null && p.isOnline()) {
                p.getInventory().addItem(GemPouches.gemPouch(Integer.parseInt(args[1])));
            }
        }

        return false;
    }


}
