package me.kayaba.guilds.enums;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.CommandExecutor;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.*;
import me.kayaba.guilds.command.admin.*;
import me.kayaba.guilds.command.admin.config.*;
import me.kayaba.guilds.command.admin.guild.*;
import me.kayaba.guilds.command.admin.guild.rank.*;
import me.kayaba.guilds.command.admin.player.*;
import me.kayaba.guilds.command.guild.*;
import me.kayaba.guilds.command.guild.rank.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.lang.reflect.*;
import java.util.*;

public final class Command extends CommandWrapperImpl {
    public static final CommandWrapper ADMIN_ACCESS = new Command(CommandAdmin.class, Permission.GUILDS_ADMIN_ACCESS, "ga", Message.CHAT_USAGE_GA_ACCESS);
    public static final CommandWrapper ADMIN_RELOAD = new Command(CommandAdminReload.class, Permission.GUILDS_ADMIN_RELOAD, Message.CHAT_USAGE_GA_RELOAD);
    public static final CommandWrapper ADMIN_SAVE = new Command(CommandAdminSave.class, Permission.GUILDS_ADMIN_SAVE, Message.CHAT_USAGE_GA_SAVE);
    public static final CommandWrapper ADMIN_CHATSPY = new Command(CommandAdminChatSpy.class, Permission.GUILDS_ADMIN_CHATSPY_SELF, Message.CHAT_USAGE_GA_CHATSPY, Flag.NOCONSOLE);
    //public static final CommandWrapper ADMIN_MIGRATE =                        new Command(CommandAdminMigrate.class,                        Permission.GUILDS_ADMIN_MIGRATE,                                     Message.CHAT_USAGE_GA_MIGRATE                                                    );
    public static final CommandWrapper ADMIN_CONFIG_ACCESS = new Command(CommandAdminConfig.class, Permission.GUILDS_ADMIN_CONFIG_ACCESS, Message.CHAT_USAGE_GA_CONFIG_ACCESS);
    public static final CommandWrapper ADMIN_CONFIG_GET = new Command(CommandAdminConfigGet.class, Permission.GUILDS_ADMIN_CONFIG_GET, Message.CHAT_USAGE_GA_CONFIG_GET);
    public static final CommandWrapper ADMIN_CONFIG_RELOAD = new Command(CommandAdminConfigReload.class, Permission.GUILDS_ADMIN_CONFIG_RELOAD, Message.CHAT_USAGE_GA_CONFIG_RELOAD);
    public static final CommandWrapper ADMIN_CONFIG_RESET = new Command(CommandAdminConfigReset.class, Permission.GUILDS_ADMIN_CONFIG_RESET, Message.CHAT_USAGE_GA_CONFIG_RESET, Flag.CONFIRM);
    public static final CommandWrapper ADMIN_CONFIG_SAVE = new Command(CommandAdminConfigSave.class, Permission.GUILDS_ADMIN_CONFIG_SAVE, Message.CHAT_USAGE_GA_CONFIG_SAVE, Flag.CONFIRM);
    public static final CommandWrapper ADMIN_CONFIG_SET = new Command(CommandAdminConfigSet.class, Permission.GUILDS_ADMIN_CONFIG_SET, Message.CHAT_USAGE_GA_CONFIG_SET);
    public static final CommandWrapper ADMIN_PLAYER_ACCESS = new Command(CommandAdminPlayer.class, Permission.GUILDS_ADMIN_PLAYER_ACCESS, Message.CHAT_USAGE_GA_PLAYER_ACCESS);
    public static final CommandWrapper ADMIN_PLAYER_SET_POINTS = new Command(CommandAdminPlayerSetPoints.class, Permission.GUILDS_ADMIN_PLAYER_SET_POINTS, Message.CHAT_USAGE_GA_PLAYER_SET_POINTS);
    public static final CommandWrapper ADMIN_GUILD_ACCESS = new Command(CommandAdminGuild.class, Permission.GUILDS_ADMIN_GUILD_ACCESS, Message.CHAT_USAGE_GA_GUILD_ACCESS);
    public static final CommandWrapper ADMIN_GUILD_ABANDON = new Command(CommandAdminGuildAbandon.class, Permission.GUILDS_ADMIN_GUILD_ABANDON, Message.CHAT_USAGE_GA_GUILD_ABANDON, Flag.CONFIRM);
    public static final CommandWrapper ADMIN_GUILD_PURGE = new Command(CommandAdminGuildPurge.class, Permission.GUILDS_ADMIN_GUILD_PURGE, Message.CHAT_USAGE_GA_GUILD_PURGE, Flag.CONFIRM);
    public static final CommandWrapper ADMIN_GUILD_BANK_PAY = new Command(CommandAdminGuildBankPay.class, Permission.GUILDS_ADMIN_GUILD_BANK_PAY, Message.CHAT_USAGE_GA_GUILD_BANK_PAY);
    public static final CommandWrapper ADMIN_GUILD_BANK_WITHDRAW = new Command(CommandAdminGuildBankWithdraw.class, Permission.GUILDS_ADMIN_GUILD_BANK_WITHDRAW, Message.CHAT_USAGE_GA_GUILD_BANK_WITHDRAW);
    public static final CommandWrapper ADMIN_GUILD_INACTIVE = new Command(CommandAdminGuildInactive.class, Permission.GUILDS_ADMIN_GUILD_INACTIVE_LIST, Message.CHAT_USAGE_GA_GUILD_INACTIVE_LIST);
    public static final CommandWrapper ADMIN_GUILD_INVITE = new Command(CommandAdminGuildInvite.class, Permission.GUILDS_ADMIN_GUILD_INVITE, Message.CHAT_USAGE_GA_GUILD_INVITE);
    public static final CommandWrapper ADMIN_GUILD_KICK = new Command(CommandAdminGuildKick.class, Permission.GUILDS_ADMIN_GUILD_KICK, Message.CHAT_USAGE_GA_GUILD_KICK);
    public static final CommandWrapper ADMIN_GUILD_LIST = new Command(CommandAdminGuildList.class, Permission.GUILDS_ADMIN_GUILD_LIST, Message.CHAT_USAGE_GA_GUILD_LIST);
    public static final CommandWrapper ADMIN_GUILD_SET_LEADER = new Command(CommandAdminGuildSetLeader.class, Permission.GUILDS_ADMIN_GUILD_SET_LEADER, Message.CHAT_USAGE_GA_GUILD_SET_LEADER);
    public static final CommandWrapper ADMIN_GUILD_SET_LIVEREGENERATIONTIME = new Command(CommandAdminGuildSetLiveRegenerationTime.class, Permission.GUILDS_ADMIN_GUILD_SET_LIVEREGENERATIONTIME, Message.CHAT_USAGE_GA_GUILD_SET_LIVEREGENERATIONTIME);
    public static final CommandWrapper ADMIN_GUILD_SET_LIVES = new Command(CommandAdminGuildSetLives.class, Permission.GUILDS_ADMIN_GUILD_SET_LIVES, Message.CHAT_USAGE_GA_GUILD_SET_LIVES);
    public static final CommandWrapper ADMIN_GUILD_SET_NAME = new Command(CommandAdminGuildSetName.class, Permission.GUILDS_ADMIN_GUILD_SET_NAME, Message.CHAT_USAGE_GA_GUILD_SET_NAME);
    public static final CommandWrapper ADMIN_GUILD_SET_POINTS = new Command(CommandAdminGuildSetPoints.class, Permission.GUILDS_ADMIN_GUILD_SET_POINTS, Message.CHAT_USAGE_GA_GUILD_SET_POINTS);
    public static final CommandWrapper ADMIN_GUILD_SET_TAG = new Command(CommandAdminGuildSetTag.class, Permission.GUILDS_ADMIN_GUILD_SET_TAG, Message.CHAT_USAGE_GA_GUILD_SET_TAG);
    public static final CommandWrapper ADMIN_GUILD_SET_TIMEREST = new Command(CommandAdminGuildSetTimerest.class, Permission.GUILDS_ADMIN_GUILD_SET_TIMEREST, Message.CHAT_USAGE_GA_GUILD_SET_TIMEREST);
    public static final CommandWrapper ADMIN_GUILD_SET_SLOTS = new Command(CommandAdminGuildSetSlots.class, Permission.GUILDS_ADMIN_GUILD_SET_SLOTS, Message.CHAT_USAGE_GA_GUILD_SET_SLOTS);
    public static final CommandWrapper ADMIN_GUILD_RESET_POINTS = new Command(CommandAdminGuildResetPoints.class, Permission.GUILDS_ADMIN_GUILD_RESET_POINTS, Message.CHAT_USAGE_GA_GUILD_RESET_POINTS, Flag.CONFIRM);
    public static final CommandWrapper ADMIN_GUILD_RANK_ACCESS = new Command(CommandAdminGuildRank.class, Permission.GUILDS_ADMIN_GUILD_RANK_ACCESS, Message.CHAT_USAGE_GA_GUILD_RANK_ACCESS);
    public static final CommandWrapper ADMIN_GUILD_RANK_DELETE = new Command(CommandAdminGuildRankDelete.class, Permission.GUILDS_ADMIN_GUILD_RANK_DELETE, Message.CHAT_USAGE_GA_GUILD_RANK_DELETE);
    public static final CommandWrapper ADMIN_GUILD_RANK_LIST = new Command(CommandAdminGuildRankList.class, Permission.GUILDS_ADMIN_GUILD_RANK_LIST, Message.CHAT_USAGE_GA_GUILD_RANK_LIST);
    public static final CommandWrapper GUILD_ACCESS = new Command(CommandGuild.class, Permission.GUILDS_GUILD_ACCESS, "guild", Message.CHAT_USAGE_GUILD_ACCESS);
    public static final CommandWrapper GUILD_ABANDON = new Command(CommandGuildAbandon.class, Permission.GUILDS_GUILD_ABANDON, "abandon", Message.CHAT_USAGE_GUILD_ABANDON, Flag.NOCONSOLE, Flag.CONFIRM);
    public static final CommandWrapper GUILD_ALLY = new Command(CommandGuildAlly.class, Permission.GUILDS_GUILD_ALLY, Message.CHAT_USAGE_GUILD_ALLY, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_BANK_PAY = new Command(CommandGuildBankPay.class, Permission.GUILDS_GUILD_BANK_PAY, Message.CHAT_USAGE_GUILD_BANK_PAY, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_BANK_WITHDRAW = new Command(CommandGuildBankWithdraw.class, Permission.GUILDS_GUILD_BANK_WITHDRAW, Message.CHAT_USAGE_GUILD_BANK_WITHDRAW, Flag.NOCONSOLE);

    public static final CommandWrapper GUILD_BUYLIFE = new Command(CommandGuildBuyLife.class, Permission.GUILDS_GUILD_BUYLIFE, Message.CHAT_USAGE_GUILD_BUY_LIFE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_BUYSLOT = new Command(CommandGuildBuySlot.class, Permission.GUILDS_GUILD_BUYSLOT, Message.CHAT_USAGE_GUILD_BUY_SLOT, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_CHATMODE = new Command(CommandGuildChatMode.class, Permission.GUILDS_GUILD_CHATMODE, Message.CHAT_USAGE_GUILD_CHATMODE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_CREATE = new Command(CommandGuildCreate.class, Permission.GUILDS_GUILD_CREATE, "create", Message.CHAT_USAGE_GUILD_CREATE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_EFFECT = new Command(CommandGuildEffect.class, Permission.GUILDS_GUILD_EFFECT, Message.CHAT_USAGE_GUILD_EFFECT, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_INFO = new Command(CommandGuildInfo.class, Permission.GUILDS_GUILD_INFO, "gi", Message.CHAT_USAGE_GUILD_INFO);
    public static final CommandWrapper GUILD_INVITE = new Command(CommandGuildInvite.class, Permission.GUILDS_GUILD_INVITE, "invite", Message.CHAT_USAGE_GUILD_INVITE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_JOIN = new Command(CommandGuildJoin.class, Permission.GUILDS_GUILD_JOIN, "join", Message.CHAT_USAGE_GUILD_JOIN, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_KICK = new Command(CommandGuildKick.class, Permission.GUILDS_GUILD_KICK, Message.CHAT_USAGE_GUILD_KICK, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_LEADER = new Command(CommandGuildLeader.class, Permission.GUILDS_GUILD_LEADER, Message.CHAT_USAGE_GUILD_LEADER, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_LEAVE = new Command(CommandGuildLeave.class, Permission.GUILDS_GUILD_LEAVE, "leave", Message.CHAT_USAGE_GUILD_LEAVE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_MENU = new Command(CommandGuildMenu.class, Permission.GUILDS_GUILD_MENU, "guildmenu", Message.CHAT_USAGE_GUILD_MENU, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_PVPTOGGLE = new Command(CommandGuildPvpToggle.class, Permission.GUILDS_GUILD_PVPTOGGLE, Message.CHAT_USAGE_GUILD_PVPTOGGLE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_TOP = new Command(CommandGuildTop.class, Permission.GUILDS_GUILD_TOP, Message.CHAT_USAGE_GUILD_TOP);
    //public static final CommandWrapper GUILD_VAULT_RESTORE =                  new Command(CommandGuildVaultRestore.class,                   Permission.GUILDS_GUILD_VAULT_RESTORE,                               Message.CHAT_USAGE_GUILD_VAULT_RESTORE,               Flag.NOCONSOLE              );
    public static final CommandWrapper GUILD_WAR = new Command(CommandGuildWar.class, Permission.GUILDS_GUILD_WAR, Message.CHAT_USAGE_GUILD_WAR, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_OPENINVITATION = new Command(CommandGuildOpenInvitation.class, Permission.GUILDS_GUILD_OPENINVITATION, Message.CHAT_USAGE_GUILD_OPENINVITATION, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_SET_NAME = new Command(CommandGuildSetName.class, Permission.GUILDS_GUILD_SET_NAME, Message.CHAT_USAGE_GUILD_SET_NAME, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_SET_TAG = new Command(CommandGuildSetTag.class, Permission.GUILDS_GUILD_SET_TAG, Message.CHAT_USAGE_GUILD_SET_TAG, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_RANK_ACCESS = new Command(CommandGuildRank.class, Permission.GUILDS_GUILD_RANK_ACCESS, Message.CHAT_USAGE_GUILD_RANK_ACCESS, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_RANK_DELETE = new Command(CommandGuildRankDelete.class, Permission.GUILDS_GUILD_RANK_DELETE, Message.CHAT_USAGE_GUILD_RANK_DELETE, Flag.NOCONSOLE);
    public static final CommandWrapper GUILD_RANK_LIST = new Command(CommandGuildRankList.class, Permission.GUILDS_GUILD_RANK_LIST, Message.CHAT_USAGE_GUILD_RANK_LIST, Flag.NOCONSOLE);
    //public static final CommandWrapper TOOL_GET =                             new Command(CommandToolGet.class,                             Permission.GUILDS_TOOL_GET,                                          Message.CHAT_USAGE_TOOL,                              Flag.NOCONSOLE              );
    public static final CommandWrapper PLAYERINFO = new Command(CommandPlayerInfo.class, Permission.GUILDS_PLAYERINFO, "playerinfo", Message.CHAT_USAGE_PLAYER_INFO);
    public static final CommandWrapper BASEGUILDS = new Command(CommandGuilds.class, Permission.GUILDS_BASEGUILDS, "guilds");
    public static final CommandWrapper CONFIRM = new Command(CommandConfirm.class, Permission.GUILDS_CONFIRM, "confirm", Message.CHAT_USAGE_CONFIRM);

    private static final Map<String, CommandWrapper> map = new HashMap<>();

    static {
        for (Field field : Command.class.getFields()) {
            if (field.getType() != CommandWrapper.class) {
                continue;
            }

            try {
                CommandWrapper commandWrapper = (CommandWrapper) field.get(null);
                commandWrapper.setName(field.getName());
                map.put(field.getName(), commandWrapper);

                CommandExecutor commandExecutor = commandWrapper.getExecutorClass().newInstance();
                PracticeServer.getInstance().getCommandManager().registerExecutor(commandWrapper, commandExecutor);

                commandWrapper.setExecutor(commandExecutor);
            } catch (IllegalAccessException | InstantiationException e) {
                LoggerUtils.exception(e);
            }
        }
    }


    private Command(Class<? extends CommandExecutor> commandExecutorClass, Permission permission, String genericCommand, MessageWrapper usageMessage, TabCompleter tabCompleter, Flag... flags) {
        this.permission = permission;
        this.usageMessage = usageMessage;
        this.genericCommand = genericCommand;
        this.tabCompleter = tabCompleter;
        this.clazz = commandExecutorClass;
        Collections.addAll(this.flags, flags);
    }


    private Command(Class<? extends CommandExecutor> commandExecutorClass, Permission permission, String genericCommand, MessageWrapper usageMessage, Flag... flags) {
        this(commandExecutorClass, permission, genericCommand, usageMessage, null, flags);
    }


    private Command(Class<? extends CommandExecutor> commandExecutorClass, Permission permission, String genericCommand, TabCompleter tabCompleter, Flag... flags) {
        this(commandExecutorClass, permission, genericCommand, null, tabCompleter, flags);
    }


    private Command(Class<? extends CommandExecutor> commandExecutorClass, Permission permission, MessageWrapper usageMessage, Flag... flags) {
        this(commandExecutorClass, permission, null, usageMessage, null, flags);
    }


    private Command(Class<? extends CommandExecutor> commandExecutorClass, Permission permission, String genericCommand, Flag... flags) {
        this(commandExecutorClass, permission, genericCommand, null, null, flags);
    }


    private Command(Class<? extends CommandExecutor> commandExecutorClass, Permission permission, Flag... flags) {
        this(commandExecutorClass, permission, null, null, null, flags);
    }

    @Override
    public void setPermission(Permission permission) {
        throw new IllegalArgumentException("Not allowed for built in commands");
    }

    @Override
    public void setExecutor(CommandExecutor executor) {
        if (getExecutor() == null) {
            super.setExecutor(executor);
        } else {
            throw new IllegalArgumentException("Not allowed for built in commands");
        }
    }

    @Override
    public void setUsageMessage(MessageWrapper message) {
        throw new IllegalArgumentException("Not allowed for built in commands");
    }

    @Override
    public void setGenericCommand(String genericCommand) {
        throw new IllegalArgumentException("Not allowed for built in commands");
    }

    @Override
    public void setTabCompleter(TabCompleter tabCompleter) {
        throw new IllegalArgumentException("Not allowed for built in commands");
    }

    @Override
    public void setName(String name) {
        if (getName() == null) {
            super.setName(name);
        } else {
            throw new IllegalArgumentException("Not allowed for built in commands");
        }
    }

    @Override
    public void setFlags(Flag... flags) {
        throw new IllegalArgumentException("Not allowed for built in commands");
    }


    public static CommandWrapper getCommand(CommandExecutor executor) {
        for (CommandWrapper wrapper : values()) {
            if (wrapper.getExecutor().equals(executor)) {
                return wrapper;
            }
        }

        return null;
    }


    public static CommandWrapper getByGenericCommand(String genericCommand) {
        for (CommandWrapper wrapper : values()) {
            if (wrapper.hasGenericCommand() && wrapper.getGenericCommand().equalsIgnoreCase(genericCommand)) {
                return wrapper;
            }
        }

        return null;
    }


    public static CommandWrapper[] values() {
        return map.values().toArray(new CommandWrapper[map.size()]);
    }


    public static void init() {

    }
}
