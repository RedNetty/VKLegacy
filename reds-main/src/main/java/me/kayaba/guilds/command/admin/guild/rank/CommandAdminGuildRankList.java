package me.kayaba.guilds.command.admin.guild.rank;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminGuildRankList extends AbstractCommandExecutor.Reversed<Guild> {
    protected final boolean admin;

    public CommandAdminGuildRankList() {
        this(true);
    }

    public CommandAdminGuildRankList(boolean admin) {
        this.admin = admin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (!admin) {
            GPlayer nPlayer = PlayerManager.getPlayer(sender);
            set(nPlayer.getGuild());

            if (!nPlayer.hasPermission(GuildPermission.RANK_LIST)) {
                Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                return;
            }
        }

        Message.CHAT_ADMIN_GUILD_RANK_LIST_HEADER.clone().setVar(VarKey.GUILD_NAME, getParameter().getName()).send(sender);
        List<MessageWrapper> itemList = new ArrayList<>();

        for (GRank rank : getParameter().getRanks()) {
            MessageWrapper row = Message.CHAT_ADMIN_GUILD_RANK_LIST_ITEM.clone();
            row.setVar(VarKey.NAME, rank.getName());
            row.setVar(VarKey.GUILD_NAME, rank.getGuild().getName());
            row.setVar(VarKey.UUID, rank.getUUID().toString());
            itemList.add(row);
        }

        Message.send(itemList, sender);
    }
}
