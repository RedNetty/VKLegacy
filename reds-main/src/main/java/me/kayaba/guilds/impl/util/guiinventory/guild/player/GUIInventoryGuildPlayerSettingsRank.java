package me.kayaba.guilds.impl.util.guiinventory.guild.player;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.*;

import java.util.*;

public class GUIInventoryGuildPlayerSettingsRank extends AbstractGUIInventory {
    private final GPlayer nPlayer;


    public GUIInventoryGuildPlayerSettingsRank(GPlayer nPlayer) {
        super(ChestGUIUtils.getChestSize(GuildPermission.values().length), Message.INVENTORY_GUI_RANKS_TITLE);
        this.nPlayer = nPlayer;
    }

    @Override
    public void generateContent() {
        Guild guild = nPlayer.getGuild();

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
                    nPlayer.setGuildRank(rank);
                    close();
                }
            });
        }
    }
}
