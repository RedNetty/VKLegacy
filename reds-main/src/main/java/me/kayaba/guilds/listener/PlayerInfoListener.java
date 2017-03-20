package me.kayaba.guilds.listener;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class PlayerInfoListener extends AbstractListener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerClickPlayer(PlayerInteractEntityEvent event) {
        if (ConfigManager.getServerVersion().isNewerThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R3) && event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        Player player = event.getPlayer();
        if ((event.getRightClicked() instanceof Player) && player.isSneaking()) {
            if (Permission.GUILDS_PLAYERINFO.has(player)) {
                GPlayer nCPlayer = PlayerManager.getPlayer(event.getRightClicked());
                plugin.getPlayerManager().sendPlayerInfo(player, nCPlayer);
            }
        }
    }
}
