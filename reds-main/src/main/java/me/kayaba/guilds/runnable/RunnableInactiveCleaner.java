package me.kayaba.guilds.runnable;

import me.bpweber.practiceserver.*;

public class RunnableInactiveCleaner implements Runnable {
    private static final PracticeServer plugin = PracticeServer.getInstance();

    @Override
    public void run() {
        plugin.getGuildManager().cleanInactiveGuilds();
    }
}
