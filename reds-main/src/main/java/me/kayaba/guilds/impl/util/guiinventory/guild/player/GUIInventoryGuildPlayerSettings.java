package me.kayaba.guilds.impl.util.guiinventory.guild.player;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;

public class GUIInventoryGuildPlayerSettings extends AbstractGUIInventory {
    private final GPlayer nPlayer;


    public GUIInventoryGuildPlayerSettings(GPlayer nPlayer) {
        super(ChestGUIUtils.getChestSize(GuildPermission.values().length), Message.INVENTORY_GUI_PLAYERSETTINGS_TITLE.clone().setVar(VarKey.PLAYER_NAME, nPlayer.getName()));
        this.nPlayer = nPlayer;
    }

    @Override
    public void generateContent() {
        if (!nPlayer.equals(getViewer())
                && (getViewer().hasPermission(GuildPermission.KICK) && Permission.GUILDS_GUILD_KICK.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_KICK.has(getViewer()))) {
            registerAndAdd(new CommandExecutor(Message.INVENTORY_GUI_PLAYERSETTINGS_ITEM_KICK, "PracticeServer:guild kick " + nPlayer.getName(), true));
        }

        if (!nPlayer.equals(getViewer())
                && (getViewer().hasPermission(GuildPermission.RANK_SET) && Permission.GUILDS_GUILD_RANK_SET.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_SET.has(getViewer()))) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_PLAYERSETTINGS_ITEM_RANK
                    .clone()
                    .setVar(VarKey.RANKNAME, nPlayer.getGuildRank() == null ? "Invalid_rank" : StringUtils.replace(nPlayer.getGuildRank().getName(), " ", "_"))) {
                @Override
                public void execute() {
                    new GUIInventoryGuildPlayerSettingsRank(nPlayer).open(getViewer());
                }
            });
        }
    }
}
