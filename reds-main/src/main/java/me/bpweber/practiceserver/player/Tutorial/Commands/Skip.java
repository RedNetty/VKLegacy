package me.bpweber.practiceserver.player.Tutorial.Commands;

import me.bpweber.practiceserver.teleport.TeleportBooks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Skip implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("Skip")) {
            Player p = (Player) sender;
            p.teleport(TeleportBooks.Cyrennica);
            p.sendMessage(ChatColor.RED + "You have skipped the tutorial! :D");
        }


        return false;
    }


}
