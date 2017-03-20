package me.kayaba.guilds.impl.util.guiinventory.guild.rank;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.*;

import java.util.*;

public class GUIInventoryGuildRankList extends AbstractGUIInventory {
    private final Guild guild;


    public GUIInventoryGuildRankList(Guild guild) {
        super(ChestGUIUtils.getChestSize(GuildPermission.values().length), Message.INVENTORY_GUI_RANKS_TITLE);
        this.guild = guild;
    }

    @Override
    public void generateContent() {
        final List<GRank> ranks = new ArrayList<>();
        ranks.addAll(PracticeServer.getInstance().getRankManager().getGenericRanks());
        ranks.addAll(guild.getRanks());

        for (final GRank rank : ranks) {
            if (guild.getCloneOfGenericRank(rank) != null) {
                continue;
            }

            ItemStack itemStack = Message.INVENTORY_GUI_RANKS_ROWITEM.setVar(VarKey.RANKNAME, StringUtils.replace(rank.getName(), " ", "_")).getItemStack();

            registerAndAdd(new Executor(itemStack) {
                @Override
                public void execute() {
                    GUIInventoryGuildRankSettings guiInventory = new GUIInventoryGuildRankSettings(rank);
                    guiInventory.open(getViewer());
                }
            });
        }

        if ((getViewer().hasPermission(GuildPermission.RANK_EDIT) && Permission.GUILDS_GUILD_RANK_EDIT.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_EDIT.has(getViewer()))
                && guild.getRanks().size() < Config.RANK_MAXAMOUNT.getInt()) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_RANKS_ADDITEM) {
                @Override
                public void execute() {
                    String rankName = Message.INVENTORY_GUI_RANKS_DEFAULTNAME.get();
                    for (GRank rank : guild.getRanks()) {
                        if (rank.getName().equals(rankName)) {
                            rankName = rankName + " " + NumberUtils.randInt(1, 999);
                        }
                    }

                    GRank rank = new GRankImpl(rankName);
                    guild.addRank(rank);
                    reopen();
                }
            });
        }
    }


    public Guild getGuild() {
        return guild;
    }
}
