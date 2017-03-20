package me.kayaba.guilds.impl.util.preparedtag;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;

import java.util.*;

public abstract class AbstractPreparedTag implements PreparedTag {
    private final Guild guild;
    private final ConfigWrapper pattern;
    private boolean leaderPrefixEnabled = true;
    private boolean leaderPrefix;
    private boolean hidden;
    private TagColor color = TagColor.NEUTRAL;


    public AbstractPreparedTag(ConfigWrapper pattern, Guild guild) {
        this.pattern = pattern;
        this.guild = guild;
    }


    public AbstractPreparedTag(ConfigWrapper pattern, Guild guild, boolean leaderPrefixEnabled) {
        this.pattern = pattern;
        this.guild = guild;
        this.leaderPrefixEnabled = leaderPrefixEnabled;
    }


    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public TagColor getColor() {
        return color;
    }

    @Override
    public boolean isLeaderPrefix() {
        return leaderPrefix;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }


    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public void setLeaderPrefix(boolean leaderPrefix) {
        this.leaderPrefix = leaderPrefix;
    }

    @Override
    public void setColor(TagColor color) {
        this.color = color;
    }

    @Override
    public void setUpFor(GPlayer nPlayer) {
        if (!nPlayer.isOnline()) {
            return;
        }

        setHidden(Permission.GUILDS_CHAT_NOTAG.has(nPlayer.getPlayer()));
        setLeaderPrefix(leaderPrefixEnabled && nPlayer.isLeader() && Config.CHAT_GUILD_LEADERPREFIX.getBoolean());
    }

    @Override
    public void setTagColorFor(GPlayer nPlayer) {
        TagColor color = TagColor.NEUTRAL;
        if (nPlayer.hasGuild() && getGuild() != null) {
            Guild onlineKayabaPlayerGuild = nPlayer.getGuild();

            if (onlineKayabaPlayerGuild.isAlly(guild)) {
                color = TagColor.ALLY;
            } else if (onlineKayabaPlayerGuild.isWarWith(guild)) {
                color = TagColor.WAR;
            } else if (getGuild().isMember(nPlayer)) {
                color = TagColor.GUILD;
            }
        }

        setColor(color);
    }

    @Override
    public String get() {
        if (isHidden() || guild == null) {
            return "";
        }

        String tag = pattern.getString();

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.RANK, isLeaderPrefix() ? Config.CHAT_LEADERPREFIX.getString() : "");
        vars.put(VarKey.COLOR, color.getConfig().getString());
        vars.put(VarKey.TAG, guild.getTag());
        tag = StringUtils.replaceVarKeyMap(tag, vars);
        tag = StringUtils.fixColors(tag);

        return tag;
    }
}
