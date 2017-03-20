package me.kayaba.guilds.impl.util.guiinventory.guild.rank;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.guiinventory.*;
import me.kayaba.guilds.impl.util.signgui.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.*;

import java.util.*;

public class GUIInventoryGuildRankSettings extends AbstractGUIInventory {
    private final GRank rank;


    public GUIInventoryGuildRankSettings(GRank rank) {
        super(9, Message.INVENTORY_GUI_RANK_SETTINGS_TITLE.clone().setVar(VarKey.RANKNAME, rank.getName()));
        this.rank = rank;
    }

    @Override
    public void generateContent() {
        if (!rank.isGeneric()
                && (getViewer().hasPermission(GuildPermission.RANK_EDIT) && Permission.GUILDS_GUILD_RANK_EDIT.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_EDIT.has(getViewer()))) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_RANK_SETTINGS_ITEM_EDITPERMISSIONS) {
                @Override
                public void execute() {
                    new GUIInventoryGuildPermissionSelect(rank).open(getViewer());
                }
            });
        }

        if ((rank.isGeneric() || !rank.equals(getGuild().getDefaultRank()))
                && !RankManager.getLeaderRank().equals(rank)
                && (getViewer().hasPermission(GuildPermission.RANK_EDIT) && Permission.GUILDS_GUILD_RANK_EDIT.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_EDIT.has(getViewer()))) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_RANK_SETTINGS_ITEM_SETDEFAULT.getItemStack()) {
                @Override
                public void execute() {
                    GRank clonedRank = rank;
                    if (rank.isGeneric()) {
                        clonedRank = cloneRank();
                    } else {
                        rank.setDefault(false);
                    }

                    if (!getGuild().getDefaultRank().isGeneric()) {
                        getGuild().getDefaultRank().setDefault(false);
                    }

                    clonedRank.setDefault(true);

                    if (rank.isGeneric()) {
                        close();
                        new GUIInventoryGuildRankSettings(clonedRank).open(getViewer());
                        return;
                    }

                    regenerate();
                }
            });
        }

        if (!RankManager.getLeaderRank().equals(rank)
                && getGuild().getRanks().size() < Config.RANK_MAXAMOUNT.getInt()
                && (getViewer().hasPermission(GuildPermission.RANK_EDIT) && Permission.GUILDS_GUILD_RANK_EDIT.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_EDIT.has(getViewer()))) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_RANK_SETTINGS_ITEM_CLONE) {
                @Override
                public void execute() {
                    GRank clonedRank = cloneRank();
                    close();
                    new GUIInventoryGuildRankSettings(clonedRank).open(getViewer());
                }
            });
        }

        if (!rank.isGeneric()) {
            if (Config.SIGNGUI_ENABLED.getBoolean()
                    && (getViewer().hasPermission(GuildPermission.RANK_EDIT) && Permission.GUILDS_GUILD_RANK_EDIT.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_EDIT.has(getViewer()))) {
                registerAndAdd(new Executor(Message.INVENTORY_GUI_RANK_SETTINGS_ITEM_RENAME) {
                    @Override
                    public void execute() {
                        final SignGUIPatternImpl pattern = new SignGUIPatternImpl(Message.SIGNGUI_GUILD_RANKS_SET_NAME.clone().setVar(VarKey.INPUT, rank.getName()));
                        plugin.getSignGUI().open(getViewer().getPlayer(), pattern, new SignGUI.SignGUIListener() {
                            @Override
                            public void onSignDone(Player player, String[] lines) {
                                rank.setName(lines[pattern.getInputLine()]);
                                close();
                                GUIInventoryGuildRankSettings gui = new GUIInventoryGuildRankSettings(rank);
                                gui.open(getViewer());
                            }
                        });
                    }
                });
            }

            if (!rank.isDefault()
                    && (getViewer().hasPermission(GuildPermission.RANK_DELETE) && Permission.GUILDS_GUILD_RANK_DELETE.has(getViewer()) || Permission.GUILDS_ADMIN_GUILD_RANK_DELETE.has(getViewer()))) {
                registerAndAdd(new Executor(Message.INVENTORY_GUI_RANK_SETTINGS_ITEM_DELETE) {
                    @Override
                    public void execute() {
                        rank.delete();
                        close();
                    }
                });
            }
        }

        if (!GUIInventoryGuildRankMembers.getMembers(getGuild(), rank).isEmpty()) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_RANK_SETTINGS_ITEM_MEMBERLIST) {
                @Override
                public void execute() {
                    new GUIInventoryGuildRankMembers(getGuild(), rank).open(getViewer());
                }
            });
        }
    }


    public Guild getGuild() {
        if (rank.isGeneric()) {
            GUIInventory previousGui = getViewer().getGuiInventoryHistory().get(getViewer().getGuiInventoryHistory().size() - 2);

            if (previousGui instanceof GUIInventoryGuildRankList) {
                return ((GUIInventoryGuildRankList) previousGui).getGuild();
            } else {
                return getViewer().getGuild();
            }
        } else {
            return rank.getGuild();
        }
    }


    private GRank cloneRank() {
        String clonePrefix = Message.INVENTORY_GUI_RANK_SETTINGS_CLONEPREFIX.get();
        String cloneName = rank.getName().startsWith(clonePrefix) || rank.isGeneric() ? rank.getName() : clonePrefix + rank.getName();

        if (StringUtils.contains(cloneName, ' ')) {
            String[] split = StringUtils.split(cloneName, ' ');

            if (NumberUtils.isNumeric(split[split.length - 1])) {
                cloneName = cloneName.substring(0, cloneName.length() - split[split.length - 1].length() - 1);
            }
        }

        GRank clone = new GRankImpl(rank);
        Guild guild = getGuild();

        boolean doubleName;
        int i = 1;
        do {
            if (i > 999) {
                break;
            }

            doubleName = false;
            for (GRank loopRank : guild.getRanks()) {
                if (!loopRank.isGeneric() && loopRank.getName().equalsIgnoreCase(clone.getName())) {
                    doubleName = true;
                }
            }

            if (doubleName) {
                clone.setName(cloneName + " " + i);
            }

            i++;
        } while (doubleName);

        guild.addRank(clone);


        for (GPlayer nPlayer : new ArrayList<>(rank.getMembers())) {
            nPlayer.setGuildRank(clone);
        }

        return clone;
    }
}
