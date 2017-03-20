package me.kayaba.guilds.runnable;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;

public class RunnableLiveRegeneration implements Runnable {
    private static final PracticeServer plugin = PracticeServer.getInstance();

    @Override
    public void run() {
        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            long lostLiveTime = guild.getLostLiveTime();

            if (lostLiveTime > 0) {
                if (NumberUtils.systemSeconds() - lostLiveTime > Config.LIVEREGENERATION_REGENTIME.getSeconds()) {
                    guild.addLive();
                    guild.resetLostLiveTime();
                }
            }
        }
    }
}
