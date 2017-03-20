package me.kayaba.guilds.runnable;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

public class RunnableAutoSave implements Runnable {
    private static final PracticeServer plugin = PracticeServer.getInstance();

    @Override
    public void run() {
        plugin.getGuildManager().save();
        plugin.getRegionManager().save();
        plugin.getPlayerManager().save();
        plugin.getRankManager().save();
        LoggerUtils.info("Saved data.");


        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            if (Permission.GUILDS_ADMIN_SAVE_NOTIFY.has(player)) {
                Message.CHAT_ADMIN_SAVE_AUTOSAVE.send(player);
            }
        }
    }
}
