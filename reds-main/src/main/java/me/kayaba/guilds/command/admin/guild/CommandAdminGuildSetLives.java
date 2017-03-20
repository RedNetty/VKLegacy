package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetLives extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        if (args.length == 0) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        if (!NumberUtils.isNumeric(args[0])) {
            Message.CHAT_ENTERINTEGER.send(sender);
            return;
        }

        int lives = Integer.parseInt(args[0]);

        if (lives < 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        if (lives > Config.GUILD_LIVES_MAX.getInt()) {
            Message.CHAT_MAXAMOUNT.clone().setVar(VarKey.AMOUNT, Config.GUILD_LIVES_MAX.getInt()).send(sender);
            return;
        }

        guild.setLives(lives);
        Message.CHAT_ADMIN_GUILD_SET_LIVES.send(sender);
        TabUtils.refresh(guild);
    }
}
