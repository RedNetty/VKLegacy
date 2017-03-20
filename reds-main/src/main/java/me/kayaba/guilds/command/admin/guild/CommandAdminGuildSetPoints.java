package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetPoints extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (Config.GUILD_PLAYERPOINTS.getBoolean()) {
            Message.CHAT_UNKNOWNCMD.send(sender);
            return;
        }

        Guild guild = getParameter();

        if (args.length != 1) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        String points = args[0];

        if (!NumberUtils.isNumeric(points)) {
            Message.CHAT_ENTERINTEGER.send(sender);
            return;
        }

        int pointsInteger = Integer.parseInt(points);

        if (pointsInteger < 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        guild.setPoints(pointsInteger);
        TabUtils.refresh(guild);

        Message.CHAT_ADMIN_GUILD_SET_POINTS.send(sender);
    }
}
