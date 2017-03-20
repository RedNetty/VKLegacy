package me.kayaba.guilds.runnable;

import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

import java.util.*;

public class RunnableRefreshTabList implements Runnable {
    @Override
    public void run() {
        TabUtils.refresh();

        Collection<Player> onlinePlayers = CompatibilityUtils.getOnlinePlayers();
        if (!onlinePlayers.isEmpty()) {
            LoggerUtils.debug("TabList refreshed (" + onlinePlayers.size() + " players)");
        }
    }
}
