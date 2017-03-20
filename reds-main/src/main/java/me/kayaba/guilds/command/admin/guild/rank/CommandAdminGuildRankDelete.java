package me.kayaba.guilds.command.admin.guild.rank;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;

public class CommandAdminGuildRankDelete extends AbstractCommandExecutor.Reversed<GRank> {
    private final boolean admin;

    public CommandAdminGuildRankDelete() {
        this(true);
    }

    public CommandAdminGuildRankDelete(boolean admin) {
        this.admin = admin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!admin && !nPlayer.hasPermission(GuildPermission.RANK_DELETE)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        GRank rank = getParameter();

        if (rank == null) {
            Message.CHAT_ADMIN_GUILD_RANK_NOTFOUND.send(sender);
            return;
        }

        Message.CHAT_ADMIN_GUILD_RANK_DELETE_SUCCESS.send(sender);
        rank.delete();
    }
}
