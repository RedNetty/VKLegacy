package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminGuildKick extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            Message.CHAT_PLAYER_ENTERNAME.send(sender);
            return;
        }

        GPlayer nPlayerKick = PlayerManager.getPlayer(args[0]);

        if (nPlayerKick == null) {
            Message.CHAT_PLAYER_NOTEXISTS.send(sender);
            return;
        }

        if (!nPlayerKick.hasGuild()) {
            Message.CHAT_PLAYER_HASNOGUILD.send(sender);
            return;
        }

        Guild guild = nPlayerKick.getGuild();

        if (nPlayerKick.isLeader()) {
            Message.CHAT_ADMIN_GUILD_KICK_LEADER.send(sender);
            return;
        }


        guild.removePlayer(nPlayerKick);

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, nPlayerKick.getName());
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.BROADCAST_GUILD_KICKED.clone().vars(vars).broadcast();


        TagUtils.refresh();
        TabUtils.refresh();
        nPlayerKick.cancelToolProgress();
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        for (GPlayer guildMember : plugin.getPlayerManager().getOnlinePlayers()) {
            if (!guildMember.isLeader() && !guildMember.equals(nPlayer)) {
                options.add(guildMember.getName());
            }
        }

        return options;
    }
}
