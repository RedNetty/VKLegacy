package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetLiveRegenerationTime extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        String timeString;
        if (args.length > 1) {
            timeString = StringUtils.join(args, " ");
        } else {
            timeString = args[0];
        }

        int seconds = StringUtils.stringToSeconds(timeString);
        long lostLiveTime = NumberUtils.systemSeconds() + (seconds - Config.LIVEREGENERATION_REGENTIME.getSeconds());

        guild.setLostLiveTime(lostLiveTime);
        TabUtils.refresh(guild);

        Message.CHAT_ADMIN_GUILD_TIMEREST_SET.send(sender);
    }
}
