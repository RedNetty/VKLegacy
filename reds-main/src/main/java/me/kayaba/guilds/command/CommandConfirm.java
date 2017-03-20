package me.kayaba.guilds.command;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;

public class CommandConfirm extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        CommandExecutorHandler handler = nPlayer.getCommandExecutorHandler();

        if (handler == null) {
            Message.CHAT_CONFIRM_NULLHANDLER.send(sender);
            return;
        }

        handler.confirm();
    }
}
