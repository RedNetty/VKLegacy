package me.kayaba.guilds.impl.util.bossbar;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

public abstract class AbstractBossBarUtils implements IBossBarUtils {
    @Override
    public void setMessage(String message) {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            setMessage(player, message);
        }
    }

    @Override
    public void setMessage(String message, float percent) {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            setMessage(player, message, percent);
        }
    }

    @Override
    public void setMessage(String message, int seconds) {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            setMessage(player, message, seconds);
        }
    }
}
