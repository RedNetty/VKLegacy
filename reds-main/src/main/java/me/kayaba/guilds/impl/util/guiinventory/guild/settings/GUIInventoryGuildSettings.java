package me.kayaba.guilds.impl.util.guiinventory.guild.settings;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.signgui.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class GUIInventoryGuildSettings extends AbstractGUIInventory {


    public GUIInventoryGuildSettings() {
        super(9, Message.INVENTORY_GUI_SETTINGS_TITLE);
    }

    @Override
    public void generateContent() {
        ItemStack togglePvpItem = (getViewer().getGuild().getFriendlyPvp()
                ? Message.INVENTORY_GUI_SETTINGS_ITEM_TOGGLEPVP_ON
                : Message.INVENTORY_GUI_SETTINGS_ITEM_TOGGLEPVP_OFF).getItemStack();

        if (Config.SIGNGUI_ENABLED.getBoolean()
                && getViewer().hasPermission(GuildPermission.SET_NAME)
                && Permission.GUILDS_GUILD_SET_NAME.has(getViewer())) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_SET_NAME) {
                @Override
                public void execute() {
                    final SignGUIPatternImpl pattern = new SignGUIPatternImpl(Message.SIGNGUI_GUILD_SETTINGS_SET_NAME.clone().setVar(VarKey.INPUT, getViewer().getGuild().getName()));
                    plugin.getSignGUI().open(getViewer().getPlayer(), pattern, new SignGUI.SignGUIListener() {
                        @Override
                        public void onSignDone(Player player, String[] lines) {
                            player.performCommand("guilds:guild setname " + lines[pattern.getInputLine()]);
                            reopen();
                        }
                    });
                }
            });
        }

        if (Config.SIGNGUI_ENABLED.getBoolean()
                && getViewer().hasPermission(GuildPermission.SET_TAG)
                && Permission.GUILDS_GUILD_SET_TAG.has(getViewer())) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_SET_TAG) {
                @Override
                public void execute() {
                    final SignGUIPatternImpl pattern = new SignGUIPatternImpl(Message.SIGNGUI_GUILD_SETTINGS_SET_TAG.clone().setVar(VarKey.INPUT, getViewer().getGuild().getTag()));
                    plugin.getSignGUI().open(getViewer().getPlayer(), pattern, new SignGUI.SignGUIListener() {
                        @Override
                        public void onSignDone(Player player, String[] lines) {
                            player.performCommand("guilds:guild settag " + lines[pattern.getInputLine()]);
                            reopen();
                        }
                    });
                }
            });
        }

        if (getViewer().hasPermission(GuildPermission.PVPTOGGLE) && Permission.GUILDS_GUILD_PVPTOGGLE.has(getViewer())) {
            registerAndAdd(new Executor(togglePvpItem) {
                @Override
                public void execute() {
                    getViewer().getPlayer().performCommand("guilds:guild pvp");
                    regenerate();
                }
            });
        }

        if (getViewer().hasPermission(GuildPermission.OPENINVITATION) && Permission.GUILDS_GUILD_OPENINVITATION.has(getViewer())) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_OPENINVITATION
                    .clone()
                    .setVar(VarKey.FLAG, Message.getOnOff(getViewer().getGuild().isOpenInvitation()))) {
                @Override
                public void execute() {
                    getViewer().getPlayer().performCommand("guilds:guild openinv");
                    regenerate();
                }
            });
        }

        if (getViewer().hasPermission(GuildPermission.BUYLIFE)
                && Permission.GUILDS_GUILD_BUYLIFE.has(getViewer())
                && getViewer().getGuild().getLives() < Config.GUILD_LIVES_MAX.getInt()) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_BUYLIFE) {
                @Override
                public void execute() {
                    getViewer().getPlayer().performCommand("guilds:guild buylife");
                    regenerate();
                }
            });
        }

        if (getViewer().hasPermission(GuildPermission.BUYSLOT)
                && Permission.GUILDS_GUILD_BUYSLOT.has(getViewer())
                && getViewer().getGuild().getSlots() < Config.GUILD_SLOTS_MAX.getInt()) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_BUYSLOT) {
                @Override
                public void execute() {
                    getViewer().getPlayer().performCommand("guilds:guild buyslot");
                    regenerate();
                }
            });
        }

        if (getViewer().hasPermission(GuildPermission.INVITE) && Permission.GUILDS_GUILD_INVITE.has(getViewer())) {
            registerAndAdd(new Executor(Message.INVENTORY_GUI_SETTINGS_ITEM_INVITE) {
                @Override
                public void execute() {
                    new GUIInventoryGuildInvite().open(getViewer());
                }
            });
        }
    }
}
