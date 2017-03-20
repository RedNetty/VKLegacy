package me.kayaba.guilds.impl.util.guiinventory;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;

public class GUIInventoryGuildPermissionSelect extends AbstractGUIInventory {
    private final GRank rank;


    public GUIInventoryGuildPermissionSelect(GRank rank) {
        super(ChestGUIUtils.getChestSize(GuildPermission.values().length), Message.INVENTORY_GUI_PERMISSIONS_TITLE.clone().setVar(VarKey.RANKNAME, rank.getName()));
        this.rank = rank;
    }

    @Override
    public void generateContent() {
        final MessageWrapper messageWrapperEnabled = Message.INVENTORY_GUI_PERMISSIONS_ITEM_ENABLED;
        final MessageWrapper messageWrapperDisabled = Message.INVENTORY_GUI_PERMISSIONS_ITEM_DISABLED;

        for (final GuildPermission perm : GuildPermission.values()) {
            MessageWrapper message;

            if (rank.hasPermission(perm)) {
                message = messageWrapperEnabled;
            } else {
                message = messageWrapperDisabled;
            }

            registerAndAdd(new Executor(message
                    .clone()
                    .setVar(VarKey.PERMNAME, Message.valueOf("INVENTORY_GUI_PERMISSIONS_NAMES_" + perm.name()).get())
                    .getItemStack()) {
                @Override
                public void execute() {
                    if (rank.hasPermission(perm)) {
                        rank.removePermission(perm);
                    } else {
                        rank.addPermission(perm);
                    }

                    reopen();
                }
            });
        }
    }
}
