package me.kayaba.guilds.command;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class CommandToolGet extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        ((Player) sender).getInventory().addItem(Config.REGION_TOOL.getItemStack());
    }
}
