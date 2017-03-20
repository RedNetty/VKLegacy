package me.kayaba.guilds.impl.util.guiinventory.guild.player;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;

import java.util.*;

public class GUIInventoryGuildPlayersList extends AbstractGUIInventory {
    protected final Guild guild;


    public GUIInventoryGuildPlayersList(Guild guild) {
        super(ChestGUIUtils.getChestSize(GuildPermission.values().length), Message.INVENTORY_GUI_PLAYERSLIST_TITLE);
        this.guild = guild;
    }

    @Override
    public void generateContent() {
        generateContent(guild.getPlayers());
    }


    public void generateContent(List<GPlayer> playerList) {
        for (final GPlayer nPlayer : playerList) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_PLAYERSLIST_ROWITEM
                    .clone()
                    .setVar(VarKey.PLAYER_NAME, nPlayer.getName())) {
                @Override
                public void execute() {
                    new GUIInventoryGuildPlayerSettings(nPlayer).open(getViewer());
                }
            });
        }
    }
}
