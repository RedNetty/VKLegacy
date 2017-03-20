package me.kayaba.guilds.command.admin.config;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;

public class CommandAdminConfigSave extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        plugin.getConfigManager().save();
        Message.CHAT_ADMIN_CONFIG_SAVED.send(sender);
    }
}
