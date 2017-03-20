package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;
import java.util.concurrent.*;

public class CommandAdminGuildInactive extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        int page = 1;
        if (args.length == 1) {
            if (NumberUtils.isNumeric(args[0])) {
                page = Integer.parseInt(args[0]);
            } else if (args[0].equalsIgnoreCase("update")) {
                if (!Permission.GUILDS_ADMIN_GUILD_INACTIVE_UPDATE.has(sender)) {
                    Message.CHAT_NOPERMISSIONS.send(sender);
                    return;
                }

                int count = 0;
                for (Guild guild : plugin.getGuildManager().getGuilds()) {
                    guild.updateInactiveTime();
                    count++;
                }

                TabUtils.refresh();

                Map<VarKey, String> vars = new HashMap<>();
                vars.put(VarKey.COUNT, String.valueOf(count));
                Message.CHAT_ADMIN_GUILD_INACTIVE_UPDATED.clone().vars(vars).send(sender);
                return;
            } else if (args[0].equalsIgnoreCase("clean")) {
                if (!Permission.GUILDS_ADMIN_GUILD_INACTIVE_CLEAN.has(sender)) {
                    Message.CHAT_NOPERMISSIONS.send(sender);
                    return;
                }

                if (plugin.getGuildManager().getGuilds().isEmpty()) {
                    Message.CHAT_GUILD_NOGUILDS.send(sender);
                    return;
                }

                plugin.getGuildManager().cleanInactiveGuilds();
                return;
            }
        }


        if (page < 1) {
            page = 1;
        }

        int perPage = 10;
        int size = plugin.getGuildManager().getGuilds().size();
        int pages_number = size / perPage;
        if (size % perPage > 0) {
            pages_number++;
        }

        Message.CHAT_ADMIN_GUILD_INACTIVE_LIST_HEADER.send(sender);
        String rowFormat = Message.CHAT_ADMIN_GUILD_INACTIVE_LIST_ITEM.get();

        int i = 0;
        boolean display = false;

        if (size > perPage) {
            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.PAGE, String.valueOf(page));
            vars.put(VarKey.NEXT, String.valueOf(page + 1));
            vars.put(VarKey.PAGES, String.valueOf(pages_number));

            if (pages_number > page) {
                Message.CHAT_ADMIN_GUILD_LIST_PAGE_HASNEXT.clone().vars(vars).send(sender);
            } else {
                Message.CHAT_ADMIN_GUILD_LIST_PAGE_NONEXT.clone().vars(vars).send(sender);
            }
        }

        for (Guild guild : plugin.getGuildManager().getMostInactiveGuilds()) {
            if ((i + 1 > (page - 1) * perPage || page == 1) && !display) {
                display = true;
                i = 0;
            }

            if (!guild.getOnlinePlayers().isEmpty()) {
                guild.updateInactiveTime();
            }

            if (display) {
                String inactiveString = StringUtils.secondsToString(NumberUtils.systemSeconds() - guild.getInactiveTime(), TimeUnit.SECONDS);

                MessageWrapper agoNowMessage = Message.CHAT_ADMIN_GUILD_INACTIVE_LIST_AGO.clone().setVar(VarKey.INACTIVE, inactiveString);
                if (inactiveString.isEmpty()) {
                    agoNowMessage = Message.CHAT_ADMIN_GUILD_INACTIVE_LIST_NOW;
                }

                Map<VarKey, String> vars = new HashMap<>();
                vars.put(VarKey.GUILD_NAME, guild.getName());
                vars.put(VarKey.PLAYER_NAME, guild.getLeader().getName());
                vars.put(VarKey.TAG, guild.getTag());
                vars.put(VarKey.PLAYERSCOUNT, String.valueOf(guild.getPlayers().size()));
                vars.put(VarKey.AGONOW, agoNowMessage.get());

                String rowMessage = MessageManager.replaceVarKeyMap(rowFormat, vars);
                MessageManager.sendMessage(sender, rowMessage);

                if (i + 1 >= perPage) {
                    break;
                }
            }

            i++;
        }
    }
}
