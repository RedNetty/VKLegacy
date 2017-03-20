package me.kayaba.guilds.impl.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.*;

import java.util.*;

public class ChatMessageImpl implements ChatMessage {
    private final Player player;
    private String message;
    private String format;
    private PreparedTag tag;
    private boolean reportToConsole = true;
    private boolean reported = false;


    public ChatMessageImpl(Player player) {
        this.player = player;
    }

    @Override
    public void send() {
        sendToPlayers(new ArrayList<>(CompatibilityUtils.getOnlinePlayers()));
    }

    @Override
    public void send(Player player) {
        if (player == null) {
            return;
        }

        player.sendMessage(parse());
        report();
    }

    @Override
    public void send(GPlayer nPlayer) {
        send(nPlayer.getPlayer());
    }

    @Override
    public void send(Guild guild) {
        sendToPlayers(guild.getOnlinePlayers());
    }

    @Override
    public void sendToGuilds(List<Guild> guildList) {
        for (Guild guild : guildList) {
            send(guild);
        }
    }

    @Override
    public void sendToPlayers(List<Player> playerList) {
        for (Player player : playerList) {
            send(player);
        }
    }

    @Override
    public void sendToKayabaPlayers(List<GPlayer> playerList) {
        for (GPlayer nPlayer : playerList) {
            send(nPlayer);
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public PreparedTag getTag() {
        return tag;
    }

    @Override
    public boolean isReportToConsole() {
        return reportToConsole;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setFormat(String format) {
        Map<String, String> vars = new HashMap<>();

        format = StringUtils.replace(format, "%1$s", "{DISPLAYNAME}");
        format = StringUtils.replace(format, "%2$s", "{MESSAGE}");

        vars.put("0", "{GROUP}");
        vars.put("1", "{WORLD}");
        vars.put("1", "{WORLDNAME}");
        vars.put("2", "{SHORTWORLDNAME}");
        vars.put("3", "{TEAMPREFIX}");
        vars.put("4", "{TEAMSUFFIX}");
        vars.put("5", "{TEAMNAME}");

        format = me.kayaba.guilds.util.StringUtils.replaceMap(format, vars);

        this.format = format;
    }

    @Override
    public void setTag(PreparedTag tag) {
        this.tag = tag;
    }

    @Override
    public void setReportToConsole(boolean reportToConsole) {
        this.reportToConsole = reportToConsole;
    }

    @Override
    public void report() {
        if (!reported && isReportToConsole()) {
            LoggerUtils.info(me.kayaba.guilds.util.StringUtils.removeColors(parse()), false);
            reported = true;
        }
    }


    private String parse() {
        String format = getFormat();
        GPlayer nPlayer = PlayerManager.getPlayer(getPlayer());
        Map<VarKey, String> vars = new HashMap<>();

        int topAmount = Config.CHAT_TOP_AMOUNT.getInt();
        if (topAmount > 0) {
            List<GPlayer> list;

            if (Config.CHAT_TOP_POINTS.getBoolean()) {
                list = PracticeServer.getInstance().getPlayerManager().getTopPlayersByPoints(topAmount);
            } else {
                list = PracticeServer.getInstance().getPlayerManager().getTopPlayersByKDR(topAmount);
            }

            int rank = list.indexOf(nPlayer);

            String rankString = "";
            if (rank != -1 && rank < topAmount) {
                rankString = Config.CHAT_TOP_FORMAT.getString();
                rankString = StringUtils.replace(rankString, "{" + VarKey.INDEX.name() + "}", String.valueOf(rank + 1));
            }

            vars.put(VarKey.GUILDS_TOP, rankString);
        }

        vars.put(VarKey.DISPLAYNAME, getPlayer().getDisplayName());
        vars.put(VarKey.PLAYER_NAME, getPlayer().getName());
        vars.put(VarKey.WORLD, getPlayer().getWorld().getName());
        vars.put(VarKey.WORLDNAME, getPlayer().getWorld().getName());
        vars.put(VarKey.PLAYER_POINTS, String.valueOf(nPlayer.getPoints()));
        vars.put(VarKey.TAG, tag.get());

        format = me.kayaba.guilds.util.StringUtils.replaceVarKeyMap(format, vars);
        format = me.kayaba.guilds.util.StringUtils.fixColors(format);
        format = StringUtils.replace(format, "{MESSAGE}", getMessage());

        return format;
    }
}
