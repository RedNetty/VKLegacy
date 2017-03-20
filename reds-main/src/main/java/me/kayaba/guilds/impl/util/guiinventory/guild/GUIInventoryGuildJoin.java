package me.kayaba.guilds.impl.util.guiinventory.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;

import java.util.*;

public class GUIInventoryGuildJoin extends AbstractGUIInventory {
    private final Collection<Guild> guildList = new HashSet<>();


    public GUIInventoryGuildJoin(Collection<Guild> guilds) {
        super(ChestGUIUtils.getChestSize(guilds.size()), Message.INVENTORY_GUI_JOIN_TITLE);
        guildList.addAll(guilds);
    }

    @Override
    public void generateContent() {
        for (final Guild guild : guildList) {
            MessageWrapper msg = Message.INVENTORY_GUI_JOIN_ROWITEM.clone()
                    .setVar(VarKey.GUILD_NAME, guild.getName())
                    .setVar(VarKey.TAG, guild.getTag())
                    .setVar(VarKey.PLAYER_NAME, guild.getLeader().getName())
                    .setVar(VarKey.GUILD_POINTS, guild.getPoints())
                    .setVar(VarKey.GUILD_LIVES, guild.getLives());
            registerAndAdd(new CommandExecutor(msg, "PracticeServer:guild join " + guild.getName(), true));
        }
    }
}
