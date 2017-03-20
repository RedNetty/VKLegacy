package me.kayaba.guilds.command.admin.config;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;

public class CommandAdminConfigReload extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        plugin.getConfigManager().reload();
        Message.CHAT_ADMIN_CONFIG_RELOADED.send(sender);
    }
}
