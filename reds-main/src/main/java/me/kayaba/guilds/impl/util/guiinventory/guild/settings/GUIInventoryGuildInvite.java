package me.kayaba.guilds.impl.util.guiinventory.guild.settings;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

public class GUIInventoryGuildInvite extends AbstractGUIInventory {

    public GUIInventoryGuildInvite() {
        super(ChestGUIUtils.getChestSize(CompatibilityUtils.getOnlinePlayers().size()), Message.INVENTORY_GUI_SETTINGS_INVITE_TITLE);
    }

    @Override
    public void generateContent() {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            final GPlayer nPlayer = PlayerManager.getPlayer(player);

            if (nPlayer.hasGuild() || plugin.getPlayerManager().isVanished(player)) {
                continue;
            }

            registerAndAdd(new CommandExecutor(Message.INVENTORY_GUI_SETTINGS_INVITE_ITEM.clone().setVar(VarKey.PLAYER_NAME, nPlayer.getName()), "PracticeServer:guild invite " + nPlayer.getName(), false));
        }
    }
}
