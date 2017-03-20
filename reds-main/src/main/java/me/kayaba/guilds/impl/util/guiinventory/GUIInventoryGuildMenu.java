package me.kayaba.guilds.impl.util.guiinventory;

import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.guiinventory.guild.*;
import me.kayaba.guilds.impl.util.guiinventory.guild.player.*;
import me.kayaba.guilds.impl.util.guiinventory.guild.rank.*;
import me.kayaba.guilds.impl.util.guiinventory.guild.settings.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class GUIInventoryGuildMenu extends AbstractGUIInventory {

    public GUIInventoryGuildMenu() {
        super(9, Message.INVENTORY_GGUI_NAME);
    }

    @Override
    public void generateContent() {
        ItemStack topItemStack = Message.INVENTORY_GUI_GUILDTOP.getItemStack();
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(topItemStack.getType());
        meta.setDisplayName(Message.HOLOGRAPHICDISPLAYS_TOPGUILDS_HEADER.prefix(false).get());
        meta.setLore(plugin.getGuildManager().getTopGuilds());
        topItemStack.setItemMeta(meta);
        registerAndAdd(new EmptyExecutor(topItemStack));

        if (getViewer().hasGuild()) {

            registerAndAdd(new Executor(Message.INVENTORY_GUI_PLAYERSLIST_ICONITEM) {
                @Override
                public void execute() {
                    new GUIInventoryGuildPlayersList(getViewer().getGuild()).open(getViewer());
                }
            });

            if (Config.RANK_GUI.getBoolean()
                    && (getViewer().hasPermission(GuildPermission.RANK_EDIT) && Permission.GUILDS_GUILD_RANK_EDIT.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_EDIT.has(getViewer()))) {
                registerAndAdd(new Executor(Message.INVENTORY_GUI_RANKS_ICONITEM) {
                    @Override
                    public void execute() {
                        new GUIInventoryGuildRankList(getViewer().getGuild()).open(getViewer());
                    }
                });
            }

            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_ICON) {
                @Override
                public void execute() {
                    new GUIInventoryGuildSettings().open(getViewer());
                }
            });
        } else {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_JOIN_ICONITEM) {
                @Override
                public void execute() {
                    new GUIInventoryGuildJoin(getViewer().getInvitedTo()).open(getViewer());
                }
            });
        }
    }
}
