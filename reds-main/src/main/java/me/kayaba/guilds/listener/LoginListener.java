package me.kayaba.guilds.listener;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.bossbar.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class LoginListener extends AbstractListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


        plugin.getPlayerManager().addIfNotExists(player);

        final GPlayer nPlayer = PlayerManager.getPlayer(player);

        PracticeServer.runTask(new Runnable() {
            @Override
            public void run() {
                plugin.getRegionManager().checkAtRegionChange(nPlayer);
            }
        });

        if (nPlayer.hasGuild()) {
            for (Player onlinePlayer : CompatibilityUtils.getOnlinePlayers()) {
                GPlayer onlineNPlayer = PlayerManager.getPlayer(onlinePlayer);

                if (onlineNPlayer.equals(nPlayer) || !onlineNPlayer.isAtRegion() || !onlineNPlayer.getAtRegion().getGuild().equals(nPlayer.getGuild())) {
                    continue;
                }

                plugin.getRegionManager().checkRaidInit(onlineNPlayer);
            }


            nPlayer.getGuild().showVaultHologram(player);
        }


        if (Config.TAGAPI_ENABLED.getBoolean()) {
            if (player.getScoreboard() == null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            TagUtils.refresh();
        }


        if (plugin.getPacketExtension() != null) {
            plugin.getPacketExtension().registerPlayer(player);
        }


        if (Config.TABLIST_ENABLED.getBoolean()) {
            nPlayer.setTabList(plugin.createTabList(nPlayer));
            TabUtils.refresh(nPlayer);
        }


        if (nPlayer.hasGuild()) {
            nPlayer.getGuild().updateInactiveTime();
        }

        BossBarUtils.removeBar(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        GPlayer nPlayer = PlayerManager.getPlayer(event.getPlayer());

        if (nPlayer.isAtRegion()) {
            plugin.getRegionManager().playerExitedRegion(nPlayer.getPlayer());
        }


        if (nPlayer.hasGuild()) {
            nPlayer.getGuild().updateInactiveTime();
        }
    }
}
