package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetTimerest extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        String timeString = "";
        if (args.length > 0) {
            timeString = StringUtils.join(args, " ");
        }

        int seconds = StringUtils.stringToSeconds(timeString);

        long newTimeRest = NumberUtils.systemSeconds() - (Config.RAID_TIMEREST.getSeconds() - seconds);

        guild.setTimeRest(newTimeRest);
        TabUtils.refresh(guild);

        Message.CHAT_ADMIN_GUILD_TIMEREST_SET.send(sender);
    }
}
