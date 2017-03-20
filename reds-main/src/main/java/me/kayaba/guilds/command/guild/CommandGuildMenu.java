package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.command.*;

public class CommandGuildMenu extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        //new GUIInventoryGuildMenu().open(nPlayer);
        sender.sendMessage(ChatColor.RED + "Still coding this.. Coming soon!");
    }
}
