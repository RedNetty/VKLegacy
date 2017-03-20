package me.kayaba.guilds.impl.util.preparedtag;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;

public class PreparedTagScoreboardImpl extends AbstractPreparedTag {
    private static final ConfigWrapper pattern = Config.CHAT_TAG_SCOREBOARD;


    public PreparedTagScoreboardImpl(Guild guild) {
        super(pattern, guild);
    }


    public PreparedTagScoreboardImpl(GPlayer nPlayer) {
        super(pattern, nPlayer.getGuild());
        setUpFor(nPlayer);
    }


    public PreparedTagScoreboardImpl(GPlayer nPlayer, boolean leaderPrefixEnabled) {
        super(pattern, nPlayer.getGuild(), leaderPrefixEnabled);
        setUpFor(nPlayer);
    }
}
