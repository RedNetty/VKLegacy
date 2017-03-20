package me.kayaba.guilds.command.admin.config;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;

public class CommandAdminConfigReset extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        plugin.getConfigManager().backupFile();

        if (plugin.getConfigManager().getConfigFile().delete()) {
            plugin.getConfigManager().reload();
        } else {
            Message.CHAT_ERROROCCURED.send(sender);
            return;
        }

        Message.CHAT_ADMIN_CONFIG_RESET.send(sender);
    }
}
