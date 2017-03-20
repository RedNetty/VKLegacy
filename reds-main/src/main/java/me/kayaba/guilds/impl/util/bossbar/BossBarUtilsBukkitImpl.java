package me.kayaba.guilds.impl.util.bossbar;

import me.kayaba.guilds.enums.*;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.*;

import java.util.*;

public class BossBarUtilsBukkitImpl extends AbstractBossBarUtils {
    private final Map<UUID, BossBar> bossBars = new HashMap<>();


    private BossBar createIfNotExists(Player player) {
        if (bossBars.containsKey(player.getUniqueId())) {
            return getBossBar(player);
        }

        BossBar bossBar = Bukkit.getServer().createBossBar("", Config.BOSSBAR_RAIDBAR_COLOR.toEnum(BarColor.class), Config.BOSSBAR_RAIDBAR_STYLE.toEnum(BarStyle.class));
        bossBar.addPlayer(player);
        bossBars.put(player.getUniqueId(), bossBar);
        return bossBar;
    }


    private BossBar getBossBar(Player player) {
        return bossBars.get(player.getUniqueId());
    }

    @Override
    public void setMessage(Player player, String message) {
        BossBar bar = createIfNotExists(player);
        bar.setTitle(message);
    }

    @Override
    public void setMessage(Player player, String message, float percent) {
        BossBar bar = createIfNotExists(player);
        bar.setTitle(message);
        bar.setProgress(percent / 100.0F);
    }

    @Override
    public void setMessage(Player player, String message, int seconds) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean hasBar(Player player) {
        return bossBars.containsKey(player.getUniqueId());
    }

    @Override
    public void removeBar(Player player) {
        if (getBossBar(player) == null) {
            return;
        }

        getBossBar(player).removeAll();
        bossBars.remove(player.getUniqueId());
    }

    @Override
    public void setHealth(Player player, float percent) {
        getBossBar(player).setProgress(percent);
    }

    @Override
    public float getHealth(Player player) {
        return (float) getBossBar(player).getProgress();
    }

    @Override
    public String getMessage(Player player) {
        return getBossBar(player).getTitle();
    }
}
