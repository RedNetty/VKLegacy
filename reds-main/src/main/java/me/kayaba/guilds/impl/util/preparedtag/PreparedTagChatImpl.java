package me.kayaba.guilds.impl.util.preparedtag;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;

public class PreparedTagChatImpl extends AbstractPreparedTag {
    private static final ConfigWrapper pattern = Config.CHAT_TAG_CHAT;


    public PreparedTagChatImpl(Guild guild) {
        super(pattern, guild);
    }


    public PreparedTagChatImpl(GPlayer nPlayer) {
        super(pattern, nPlayer.getGuild());
        setUpFor(nPlayer);
    }


    public PreparedTagChatImpl(GPlayer nPlayer, boolean leaderPrefixEnabled) {
        super(pattern, nPlayer.getGuild(), leaderPrefixEnabled);
        setUpFor(nPlayer);
    }
}
