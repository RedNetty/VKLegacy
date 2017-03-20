package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.guiinventory.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;

public class CommandGuildRequiredItems extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        new GUIInventoryRequiredItems(GroupManager.getGroup(sender).get(GroupImpl.Key.CREATE_ITEMS)).open(PlayerManager.getPlayer(sender));
    }
}
