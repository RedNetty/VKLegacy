package me.kayaba.guilds.command.guild;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.concurrent.*;

public class CommandGuildInfo extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        String guildName;
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (args.length > 0) {
            guildName = args[0];
        } else {
            if (!(sender instanceof Player)) {
                Message.CHAT_CMDFROMCONSOLE.send(sender);
                return;
            }

            if (nPlayer.hasGuild()) {
                guildName = nPlayer.getGuild().getName();
            } else {
                Message.CHAT_GUILD_NOTINGUILD.send(sender);
                return;
            }
        }


        Guild guild = GuildManager.getGuildFind(guildName);

        if (guild == null) {
            Message.CHAT_GUILD_NAMENOTEXIST.send(sender);
            return;
        }

        Map<VarKey, String> vars = new HashMap<>();
        List<String> guildInfoMessages;

        if ((sender instanceof Player && nPlayer.hasGuild() && guild.isMember(nPlayer)) || Permission.GUILDS_ADMIN_GUILD_FULLINFO.has(sender)) {
            guildInfoMessages = Message.CHAT_GUILDINFO_FULLINFO.getList();
        } else {
            guildInfoMessages = Message.CHAT_GUILDINFO_INFO.getList();
        }

        MessageManager.sendPrefixMessage(sender, guildInfoMessages.get(0));

        int i;
        List<GPlayer> playerList = guild.getPlayers();
        String playerColor;


        MessageWrapper playerRowFormat = Message.CHAT_GUILDINFO_ROW_PLAYER;
        Collection<MessageWrapper> playerNamesSet = new HashSet<>();

        if (!playerList.isEmpty()) {
            for (GPlayer nPlayerList : guild.getPlayers()) {
                if (nPlayerList.isOnline() && !plugin.getPlayerManager().isVanished(nPlayerList.getPlayer())) {
                    playerColor = Message.CHAT_GUILDINFO_PLAYERCOLOR_ONLINE.get();
                } else {
                    playerColor = Message.CHAT_GUILDINFO_PLAYERCOLOR_OFFLINE.get();
                }

                playerNamesSet.add(playerRowFormat
                        .clone()
                        .setVar(VarKey.COLOR, playerColor)
                        .setVar(VarKey.PLAYER_NAME, nPlayerList.getName())
                        .setVar(VarKey.LEADER_PREFIX, nPlayerList.isLeader() ? Message.CHAT_GUILDINFO_LEADERPREFIX.get() : "")
                );
            }
        }

        String players = StringUtils.join(playerNamesSet, Message.CHAT_GUILDINFO_PLAYERSEPARATOR);


        String allies = "";
        if (!guild.getAllies().isEmpty()) {
            MessageWrapper allyFormat = Message.CHAT_GUILDINFO_ROW_ALLY;
            Collection<MessageWrapper> allySet = new HashSet<>();

            for (Guild allyGuild : guild.getAllies()) {
                allySet.add(allyFormat.clone().setVar(VarKey.GUILD_NAME, allyGuild.getName()));
            }

            allies = StringUtils.join(allySet, Message.CHAT_GUILDINFO_PLAYERSEPARATOR);
        }


        String wars = "";
        if (!guild.getWars().isEmpty()) {
            MessageWrapper warFormat = Message.CHAT_GUILDINFO_ROW_WAR;
            final Collection<MessageWrapper> warSet = new HashSet<>();

            for (Guild war : guild.getWars()) {
                warSet.add(warFormat.clone().setVar(VarKey.GUILD_NAME, war.getName()));
            }

            wars = StringUtils.join(warSet, Message.CHAT_GUILDINFO_PLAYERSEPARATOR);
        }

        vars.put(VarKey.RANK, "");
        vars.put(VarKey.GUILD_NAME, guild.getName());
        vars.put(VarKey.LEADER, guild.getLeader().getName());
        vars.put(VarKey.TAG, guild.getTag());
        vars.put(VarKey.MONEY, String.valueOf(guild.getMoney()));
        vars.put(VarKey.PLAYERS, players);
        vars.put(VarKey.PLAYERSCOUNT, String.valueOf(guild.getPlayers().size()));
        vars.put(VarKey.SLOTS, String.valueOf(guild.getSlots()));
        vars.put(VarKey.GUILD_POINTS, String.valueOf(guild.getPoints()));
        vars.put(VarKey.GUILD_LIVES, String.valueOf(guild.getLives()));
        vars.put(VarKey.GUILD_OPENINVITATION, Message.getOnOff(guild.isOpenInvitation()));


        long liveRegenerationTime = Config.LIVEREGENERATION_REGENTIME.getSeconds() - (NumberUtils.systemSeconds() - guild.getLostLiveTime());
        String liveRegenerationString = StringUtils.secondsToString(liveRegenerationTime);

        long timeWait = (guild.getTimeRest() + Config.RAID_TIMEREST.getSeconds()) - NumberUtils.systemSeconds();

        vars.put(VarKey.LIVEREGENERATIONTIME, liveRegenerationString);
        vars.put(VarKey.GUILD_TIME_REST, StringUtils.secondsToString(timeWait));


        long createdAgo = NumberUtils.systemSeconds() - guild.getTimeCreated();
        long protectionLeft = Config.GUILD_CREATEPROTECTION.getSeconds() - createdAgo;

        vars.put(VarKey.CREATEDAGO, StringUtils.secondsToString(createdAgo, TimeUnit.HOURS));
        vars.put(VarKey.PROTLEFT, StringUtils.secondsToString(protectionLeft, TimeUnit.HOURS));


        Location homeLocation = guild.getHome();
        if (homeLocation != null) {
            vars.put(VarKey.X, String.valueOf(homeLocation.getBlockX()));
            vars.put(VarKey.Y, String.valueOf(homeLocation.getBlockY()));
            vars.put(VarKey.Z, String.valueOf(homeLocation.getBlockZ()));
        }


        vars.put(VarKey.ALLIES, allies);
        vars.put(VarKey.WARS, wars);

        for (i = 1; i < guildInfoMessages.size(); i++) {
            boolean skip = false;
            String guildInfoMessage = guildInfoMessages.get(i);


            if (liveRegenerationTime <= 0 && guildInfoMessage.contains(VarKey.LIVEREGENERATIONTIME.getNameWithBrackets())) {
                skip = true;
            }


            if (timeWait <= 0 && guildInfoMessage.contains(VarKey.GUILD_TIME_REST.getNameWithBrackets())) {
                skip = true;
            }


            if ((guildInfoMessage.contains(VarKey.X.getNameWithBrackets())
                    || guildInfoMessage.contains(VarKey.Y.getNameWithBrackets())
                    || guildInfoMessage.contains(VarKey.Z.getNameWithBrackets()))
                    && guild.getHome() == null) {
                skip = true;
            }


            if (guildInfoMessage.contains(VarKey.ALLIES.getNameWithBrackets()) && allies.isEmpty()) {
                skip = true;
            }


            if (guildInfoMessage.contains(VarKey.WARS.getNameWithBrackets()) && wars.isEmpty()) {
                skip = true;
            }

            if (guildInfoMessage.contains(VarKey.PROTLEFT.getNameWithBrackets()) && protectionLeft <= 0) {
                skip = true;
            }

            if (guildInfoMessage.contains(VarKey.CREATEDAGO.getNameWithBrackets()) && protectionLeft > 0) {
                skip = true;
            }

            if (!skip) {
                guildInfoMessage = MessageManager.replaceVarKeyMap(guildInfoMessage, vars);
                MessageManager.sendMessage(sender, guildInfoMessage);
            }
        }
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();

        for (Guild guild : PracticeServer.getInstance().getGuildManager().getGuilds()) {
            options.add(guild.getTag().toLowerCase());
            options.add(guild.getName().toLowerCase());
        }

        int limit = 0;
        for (GPlayer nPlayerLoop : PracticeServer.getInstance().getPlayerManager().getPlayers()) {
            if (limit > 100) {
                break;
            }

            if (!nPlayerLoop.getName().startsWith(args[args.length - 1])) {
                continue;
            }

            if (!nPlayerLoop.hasGuild()) {
                continue;
            }

            options.add(nPlayerLoop.getName());
            limit++;
        }

        return options;
    }
}
