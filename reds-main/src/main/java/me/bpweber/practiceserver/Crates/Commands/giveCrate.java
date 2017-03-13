package me.bpweber.practiceserver.Crates.Commands;

import me.bpweber.practiceserver.Crates.CratesMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class giveCrate implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("giveCrate") && sender.getName().equalsIgnoreCase("Fatherhood") || sender instanceof ConsoleCommandSender) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null && p.isOnline()) {
                p.getInventory().addItem(CratesMain.createCrate(Integer.parseInt(args[1])));
            }
        }

        return false;
    }


}
