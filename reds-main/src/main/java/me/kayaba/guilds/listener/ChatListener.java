package me.kayaba.guilds.listener;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.preparedtag.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class ChatListener extends AbstractListener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        GPlayer nPlayer = PlayerManager.getPlayer(player);
        event.setCancelled(true);
        Guild guild = nPlayer.getGuild();

        PreparedTag preparedTag = new PreparedTagChatImpl(nPlayer);

        String prefixChatGuild = Config.CHAT_GUILD_PREFIX.getString();
        String prefixChatAlly = Config.CHAT_ALLY_PREFIX.getString();

        String msg = event.getMessage();
        boolean isAllyPrefix = msg.startsWith(prefixChatAlly);
        boolean isGuildPrefix = msg.startsWith(prefixChatGuild) && !isAllyPrefix;

        if (nPlayer.hasGuild()
                && !isGuildPrefix
                && (isAllyPrefix || nPlayer.getPreferences().getChatMode() == ChatMode.ALLY)
                && Config.CHAT_ALLY_ENABLED.getBoolean()) {
            preparedTag.setLeaderPrefix(preparedTag.isLeaderPrefix() && Config.CHAT_ALLY_LEADERPREFIX.getBoolean());

            preparedTag.setColor(TagColor.NEUTRAL);
            String cFormat = Config.CHAT_ALLY_FORMAT
                    .setVar(VarKey.GUILD_TAG, preparedTag.get())
                    .setVar(VarKey.PLAYER_NAME, nPlayer.getName())
                    .getString();


            if (nPlayer.getPreferences().getChatMode() != ChatMode.ALLY) {
                msg = msg.substring(prefixChatAlly.length(), msg.length());

                if (msg.length() == 0) {
                    return;
                }
            }

            for (GPlayer nPlayerLoop : plugin.getPlayerManager().getOnlinePlayers()) {
                if (nPlayerLoop.getPreferences().getSpyMode() || (nPlayerLoop.hasGuild() && (nPlayerLoop.getGuild().isAlly(guild) || guild.isMember(nPlayerLoop)))) {
                    nPlayerLoop.getPlayer().sendMessage(cFormat + msg);
                }
            }

            return;
        } else if (nPlayer.hasGuild()
                && (isGuildPrefix || nPlayer.getPreferences().getChatMode() == ChatMode.GUILD)
                && Config.CHAT_GUILD_ENABLED.getBoolean()) {
            String rank = Config.CHAT_GUILD_LEADERPREFIX.getBoolean() && nPlayer.isLeader() ? Config.CHAT_LEADERPREFIX.getString() : "";

            String cFormat = Config.CHAT_GUILD_FORMAT
                    .setVar(VarKey.LEADER_PREFIX, rank)
                    .setVar(VarKey.PLAYER_NAME, nPlayer.getName())
                    .getString();


            if (nPlayer.getPreferences().getChatMode() != ChatMode.GUILD) {
                msg = msg.substring(prefixChatGuild.length(), msg.length());

                if (msg.length() == 0) {
                    return;
                }
            }

            for (GPlayer nPlayerLoop : plugin.getPlayerManager().getOnlinePlayers()) {
                if (guild.isMember(nPlayerLoop) || nPlayerLoop.getPreferences().getSpyMode()) {
                    nPlayerLoop.getPlayer().sendMessage(cFormat + msg);
                }
            }

            return;
        }


        if (Config.CHAT_ADVANCED.getBoolean()) {
            ChatMessage chatMessage = new ChatMessageImpl(player);
            chatMessage.setTag(preparedTag);
            chatMessage.setFormat(event.getFormat());
            chatMessage.setMessage(event.getMessage());

            for (GPlayer onlineKayabaPlayer : plugin.getPlayerManager().getOnlinePlayers()) {
                preparedTag.setTagColorFor(onlineKayabaPlayer);
                preparedTag.setHidden(Permission.GUILDS_CHAT_NOTAG.has(player));

                chatMessage.send(onlineKayabaPlayer);
            }
        } else {
            event.setCancelled(false);
            String format = event.getFormat();
            format = org.apache.commons.lang.StringUtils.replace(format, "{TAG}", preparedTag.get());
            event.setFormat(format);
        }
    }

    @EventHandler
    public void onCommandExecute(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().substring(1, event.getMessage().length());

        if (cmd.contains(" ")) {
            String[] split = org.apache.commons.lang.StringUtils.split(cmd, ' ');
            cmd = split[0];
        }

        final GPlayer nPlayer = PlayerManager.getPlayer(event.getPlayer());
        if (!nPlayer.getPreferences().getBypass() && Config.REGION_BLOCKEDCMDS.getStringList().contains(cmd.toLowerCase())
                && nPlayer.isAtRegion()
                && (!nPlayer.hasGuild() || !nPlayer.getAtRegion().getGuild().isMember(nPlayer) && !nPlayer.getAtRegion().getGuild().isAlly(nPlayer.getGuild()))) {
            Message.CHAT_REGION_BLOCKEDCMD.send(event.getPlayer());
            event.setCancelled(true);
        }

        if (plugin.getCommandManager().existsAlias(cmd)) {
            event.setMessage(event.getMessage().replaceFirst(cmd, plugin.getCommandManager().getMainCommand(cmd)));
        }


        if (plugin.getDependencyManager().isEnabled(Dependency.ESSENTIALS)
                && (cmd.equalsIgnoreCase("vanish")
                || cmd.equalsIgnoreCase("v")
                || cmd.equalsIgnoreCase("essentials:vanish")
                || cmd.equalsIgnoreCase("essentials:v"))) {
            PracticeServer.runTask(new Runnable() {
                @Override
                public void run() {
                    plugin.getRegionManager().checkAtRegionChange(nPlayer);
                }
            });
        }
    }
}
